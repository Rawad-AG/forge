package dev.forge.plugins_manager.load;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import dev.forge.engine.cli.input.Prompter;
import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.engine.core.ForgePlugin;
import dev.forge.plugins_manager.utils.PathResolver;
import picocli.CommandLine.Command;

@Command(name = "load", description = "Load the current plugin to the forge directory")
public class LoadCommand implements Runnable {
    private final Console console = ForgeEngine.context().console();
    private final Prompter prompter = ForgeEngine.context().prompter();

    @Override
    public void run() {
        Path cwd = Path.of("").toAbsolutePath();
        Path targetDir = cwd.resolve("target");

        if (!Files.exists(targetDir) || !Files.isDirectory(targetDir))
            console.fatal("No target directory found in " + cwd);

        List<Path> jars = new ArrayList<>();
        try (var stream = Files.list(targetDir)) {
            jars = stream
                    .filter(p -> p.getFileName().toString().endsWith(".jar"))
                    .toList();
        } catch (IOException e) {
            console.fatal("Failed to scan target directory");
        }

        if (jars.isEmpty())
            console.fatal("No JAR files found in " + targetDir);

        Path selectedJar;

        if (jars.size() == 1)
            selectedJar = jars.get(0);

        else {
            console.info("Multiple plugins found:");
            for (int i = 0; i < jars.size(); i++)
                console.println("\t[" + i + "] " + jars.get(i).getFileName());

            int choice = -1;
            String c = prompter.askRequired("Select plugin to install");

            try {
                choice = Integer.parseInt(c);
            } catch (NumberFormatException e) {
                console.fatal("Invalid selection");
            }

            if (choice < 0 || choice >= jars.size())
                console.fatal("Selection out of range");

            selectedJar = jars.get(choice);
        }

        Path forgePluginsDir = PathResolver.ForgePluginsPath();

        try {
            Files.createDirectories(forgePluginsDir);
        } catch (IOException e) {
            console.fatal("Failed to create plugins directory");
        }

        ForgePlugin plugin = ForgeEngine.pluginLoader().loadFromJar(selectedJar);
        Path targetPlugin = forgePluginsDir.resolve(plugin.getName() + ".jar");

        if (Files.exists(targetPlugin)) {
            boolean answer = prompter.confirmOrDefault("Plugin already exists. Overwrite?", false);

            if (!answer)
                console.fatal("Installation cancelled.");

        }
        try {
            Files.copy(selectedJar, targetPlugin, StandardCopyOption.REPLACE_EXISTING);
            console.success("Installed plugin: " + targetPlugin.getFileName());
        } catch (IOException e) {
            console.fatal("Failed to install plugin");
        }
    }
}
