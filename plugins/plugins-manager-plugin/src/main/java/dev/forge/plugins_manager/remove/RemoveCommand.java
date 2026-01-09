package dev.forge.plugins_manager.remove;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.plugins_manager.utils.PathResolver;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "remove", description = "Remove one or more plugins")
public class RemoveCommand implements Runnable {

    @Parameters(arity = "1..*", paramLabel = "PLUGIN", description = "Plugins to remove")
    private List<String> plugins;

    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        Path forgePluginsDir = PathResolver.ForgePluginsPath();

        for (String plugin : plugins) {
            Path jarPath = forgePluginsDir.resolve(plugin + ".jar");

            if (Files.exists(jarPath)) {
                try {
                    Files.delete(jarPath);
                    console.info("Removed plugin: " + plugin);
                } catch (IOException e) {
                    System.err.println("Failed to remove plugin " + plugin + ": " + e.getMessage());
                }
            } else
                console.error("Plugin not found: " + plugin);
        }
    }
}
