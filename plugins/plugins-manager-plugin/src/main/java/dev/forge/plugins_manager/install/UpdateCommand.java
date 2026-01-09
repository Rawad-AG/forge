package dev.forge.plugins_manager.install;

import java.nio.file.Path;
import java.util.List;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.engine.core.ForgePlugin;
import dev.forge.plugins_manager.install.metadata.ForgeMetadata;
import dev.forge.plugins_manager.install.metadata.MetaDataLoader;
import dev.forge.plugins_manager.install.metadata.Plugin;
import dev.forge.plugins_manager.utils.PathResolver;
import picocli.CommandLine.Command;

@Command(name = "update", description = "update your plugins to the latest release")
public class UpdateCommand implements Runnable {

    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        ForgeMetadata meta = new MetaDataLoader().load();
        List<ForgePlugin> plugins = ForgeEngine.pluginLoader().loadAll();

        for (ForgePlugin p : plugins) {
            String pluginName = p.getName();
            Plugin plugin = meta.getPlugin(pluginName);
            if (plugin == null) {
                console.error("Plugin not found: " + pluginName);
                continue;
            }

            String downloadUrl = plugin.getLatest();
            try {
                Path target = PathResolver.ForgePluginsPath().resolve(pluginName + ".jar");
                new Downloader().download(downloadUrl, target, pluginName);
                console.success("Updated: " + pluginName);
            } catch (Exception e) {
                console.error("unable to download plugin", e);
            }
        }
    }
}