package dev.forge.requester;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.collection.CollectionsCommand;
import dev.forge.requester.http.HttpCommand;
import picocli.CommandLine.Command;

@Command(name = "requester", mixinStandardHelpOptions = true, subcommands = {
        HttpCommand.class,
        CollectionsCommand.class
})
public class RequesterCommand implements Runnable {
    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        console.error("Usage: forge requester <command> [...option]");
    }

}
