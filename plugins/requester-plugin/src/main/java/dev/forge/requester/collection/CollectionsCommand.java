package dev.forge.requester.collection;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.collection.commands.AddCommand;
import dev.forge.requester.collection.commands.EditCommand;
import dev.forge.requester.collection.commands.ListCommand;
import dev.forge.requester.collection.commands.RunCommand;
import picocli.CommandLine.Command;

@Command(name = "collections", aliases = { "col" }, mixinStandardHelpOptions = true, subcommands = {
        ListCommand.class, AddCommand.class, EditCommand.class, RunCommand.class
})
public class CollectionsCommand implements Runnable {

    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        console.info("Usage forge requester collections [subcommand] [...options]");
    }

}
