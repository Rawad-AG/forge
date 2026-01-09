package dev.forge.plugins_manager.list;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List installed plugins")
public class ListCommand implements Runnable {
    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        ForgeEngine.pluginLoader().loadAll().forEach(p -> console.info(p.getName() + " v" + p.getVersion()));
    }
}
