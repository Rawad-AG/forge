package dev.forge.archetype.generators;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import dev.forge.engine.cli.input.Prompts;
import dev.forge.engine.cli.output.Console;
import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.engine.utils.StringUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Generator {

    private final ForgeConfigLoader config = ForgeEngine.context().config();
    private final Console console = ForgeEngine.context().console();
    private final Prompts prompter = ForgeEngine.context().prompter();

    public void generate(Path targetDir, String template) {
        try {
            Path templateRoot = Path.of(config.getPath("forge.directory"), "archetypes", "templates", template);

            Path sourceTemplateDir = templateRoot.resolve("template");
            Path ctxFile = templateRoot.resolve("ctx.properties");

            if (!Files.isDirectory(sourceTemplateDir))
                throw new RuntimeException("Template does not exist: " + sourceTemplateDir);

            if (Files.exists(targetDir) && !Files.isDirectory(targetDir))
                throw new RuntimeException("Target path exists but is not a directory: " + targetDir);

            Files.createDirectories(targetDir);

            Map<String, String> ctx = loadContext(ctxFile);

            renderTemplate(sourceTemplateDir, targetDir, ctx);

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate plugin", e);
        }
    }

    private Map<String, String> loadContext(Path ctxFile) {
        Map<String, String> ctx = new HashMap<>();

        if (!Files.exists(ctxFile))
            return ctx;

        Properties props = new Properties();

        try {
            List<String> lines = Files.readAllLines(ctxFile);
            props.load(new StringReader(String.join("\n", lines)));

            for (String name : props.stringPropertyNames()) {
                String[] parts = props.getProperty(name).split(":");
                String val = null;
                for (String p : parts) {
                    if (p.startsWith("askOrDefault")) {
                        String[] args = p.substring(p.indexOf("(") + 1, p.indexOf(")")).split(",");
                        if (args.length != 2)
                            throw new RuntimeException();
                        val = prompter.askOrDefault(args[0], args[1]);
                    }

                    if (p.startsWith("askRequired")) {
                        String[] args = p.substring(p.indexOf("(") + 1, p.indexOf(")")).split(",");
                        if (args.length != 1)
                            throw new RuntimeException();
                        val = prompter.askRequired(args[0]);
                    }

                    if (val != null && "capital".equals(p))
                        ctx.put(name + "-capital", StringUtils.capitalize(val).trim());

                    if (val != null && "upper".equals(p))
                        ctx.put(name + "-upper", val.toUpperCase().trim());

                    if (val != null && "lower".equals(p))
                        ctx.put(name + "-lower", val.toLowerCase().trim());

                    if (val != null)
                        ctx.put(name, val.trim());

                }
            }
            return ctx;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read context file: " + ctxFile, e);
        }

    }

    private void renderTemplate(Path sourceDir, Path targetDir, Map<String, String> ctx) {
        MustacheFactory ms = new DefaultMustacheFactory();

        try (var paths = Files.walk(sourceDir)) {
            paths.forEach(source -> processPath(sourceDir, source, targetDir, ms, ctx));
        } catch (IOException e) {
            throw new RuntimeException("Failed to render template", e);
        }
    }

    private void processPath(Path sourceRoot, Path source, Path targetRoot, MustacheFactory ms,
            Map<String, String> ctx) {
        Path relative = sourceRoot.relativize(source);
        Path renderedRelative = renderRelativePath(relative, ms, ctx);
        Path target = targetRoot.resolve(renderedRelative);

        try {
            if (Files.isDirectory(source)) {
                Files.createDirectories(target);
                console.info("Created directory: " + target.toAbsolutePath());
                return;
            }

            if (source.getFileName().toString().endsWith(".mustache")) {
                Path renderedTarget = target.resolveSibling(
                        target.getFileName().toString().replaceFirst("\\.mustache$", ""));

                try (var reader = Files.newBufferedReader(source);
                        var writer = Files.newBufferedWriter(renderedTarget)) {

                    Mustache mustache = ms.compile(reader, source.getFileName().toString());
                    mustache.execute(writer, ctx).flush();
                }

                console.info("Rendered template: " + renderedTarget);

            } else {
                Files.copy(source, target);
                console.info("Copied file: " + target);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to process: " + source, e);
        }
    }

    private Path renderRelativePath(Path relative, MustacheFactory ms, Map<String, String> ctx) {
        Path result = Path.of("");

        for (Path part : relative) {
            result = result.resolve(renderName(part.toString(), ms, ctx));
        }

        return result;
    }

    private String renderName(String name, MustacheFactory ms, Map<String, String> ctx) {
        if (!name.contains("{{")) {
            return name;
        }

        StringWriter writer = new StringWriter();
        Mustache mustache = ms.compile(new StringReader(name), name);
        mustache.execute(writer, ctx);
        return writer.toString();
    }
}
