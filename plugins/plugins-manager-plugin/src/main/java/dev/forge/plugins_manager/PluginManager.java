package dev.forge.plugins_manager;

import dev.forge.engine.core.ForgePlugin;
import picocli.CommandLine;

public class PluginManager implements ForgePlugin {

    @Override
    public String getName() {
        return "plugins-manager";
    }

    @Override
    public String getVersion() {
        return "0.0.1-SNAPSHOT";
    }

    @Override
    public int execute(String[] args) {
        return new CommandLine(new PluginManagerCommand()).execute(args);
    }
}
