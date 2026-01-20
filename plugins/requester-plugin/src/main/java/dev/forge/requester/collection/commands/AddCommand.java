package dev.forge.requester.collection.commands;

import java.nio.file.Path;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.collection.utils.CollectionAdder;
import dev.forge.requester.collection.utils.CollectionLoader;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "add", mixinStandardHelpOptions = true)
public class AddCommand implements Runnable {
    @Parameters(index = "0", description = "specify what you want to add: collection,folder, request")
    String type;

    @Option(names = "--dir", required = false)
    String dir;

    private final Console console = ForgeEngine.context().console();
    private final ForgeConfigLoader conf = ForgeEngine.context().config();

    @Override
    public void run() {
        try {
            if (dir == null || dir.isBlank())
                dir = conf.getString("requester.collections.directory");

            var loader = new CollectionLoader(Path.of(dir));
            loader.loadAll();
            var adder = new CollectionAdder(loader);

            switch (type) {
                case "collection" -> adder.addCollection(dir);
                case "folder" -> adder.addFolder(dir);
                case "request", "req" -> adder.addRequest(dir);
            }
        } catch (Exception e) {
            console.error("Error:", e);
        }
    }

}
