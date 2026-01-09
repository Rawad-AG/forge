package dev.forge.plugins_manager;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.plugins_manager.install.InstallCommand;
import dev.forge.plugins_manager.install.PullCommand;
import dev.forge.plugins_manager.install.UpdateCommand;
import dev.forge.plugins_manager.list.ListCommand;
import dev.forge.plugins_manager.load.LoadCommand;
import dev.forge.plugins_manager.remove.RemoveCommand;
import lombok.Setter;
import picocli.CommandLine.Command;

@Setter
@Command(name = "plugin-manager", aliases = { "pm" }, mixinStandardHelpOptions = true, subcommands = {
        ListCommand.class,
        RemoveCommand.class,
        LoadCommand.class,
        InstallCommand.class,
        PullCommand.class,
        UpdateCommand.class
})
public class PluginManagerCommand implements Runnable {
    private Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        console.fatal("Usage plugins-manager [-hv] <command> [...options]");
    }
}
