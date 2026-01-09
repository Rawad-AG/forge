package dev.forge.make.generators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true, chain = true)
public class FileMaker {
    private boolean override = false;
    private Console console = ForgeEngine.context().console();

    public void generate(Path target, String template, Map<String, Object> context) {
        File dir = new File(target.getParent().toString());
        if (!dir.exists() && !dir.mkdirs())
            throw new RuntimeException("unable to create directory " + target.getParent().toAbsolutePath());

        File file = new File(dir, target.getFileName().toString());
        if (file.exists() && !override)
            throw new RuntimeException("file already exists use");

        InputStream is = null;
        try {
            File templateFile = new File(template);
            if (templateFile.exists())
                is = new FileInputStream(templateFile);
            else
                is = getClass().getClassLoader().getResourceAsStream(template);

            if (is == null)
                throw new RuntimeException("Template not found.");

            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new InputStreamReader(is), "service");

            try (Writer writer = new FileWriter(file)) {
                mustache.execute(writer, context).flush();
            }

            console.success("Created " + file.getAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
