package dev.forge.plugins_manager.install;

import java.nio.file.Path;
import java.util.List;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.plugins_manager.install.metadata.ForgeMetadata;
import dev.forge.plugins_manager.install.metadata.MetaDataLoader;
import dev.forge.plugins_manager.install.metadata.Plugin;
import dev.forge.plugins_manager.utils.PathResolver;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "install", aliases = { "i" }, description = "Install one or more plugins")
public class InstallCommand implements Runnable {

    @Parameters(arity = "1..*", paramLabel = "PLUGIN", description = "Plugins to install")
    private List<String> plugins;

    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        ForgeMetadata meta = new MetaDataLoader().load();
        for (String p : plugins) {
            String[] parts = p.split(":");
            String pluginName = parts[0];
            String version = parts.length == 2 ? parts[1] : "latest";

            Plugin plugin = meta.getPlugin(pluginName);
            if (plugin == null) {
                console.error("Plugin not found: " + pluginName + ":" + version);
                continue;
            }

            String downloadUrl = plugin.resolve(version);
            if (downloadUrl == null || downloadUrl.isBlank()) {
                console.error("Version of plugin not found: " + pluginName + "-" + version);
                continue;
            }

            try {
                Path target = PathResolver.ForgePluginsPath().resolve(pluginName + ".jar");
                new Downloader().download(downloadUrl, target, pluginName);
                console.success("Installed: " + pluginName + ":" + version);
            } catch (Exception e) {
                console.error("unable to download plugin", e);
            }
        }
    }
}