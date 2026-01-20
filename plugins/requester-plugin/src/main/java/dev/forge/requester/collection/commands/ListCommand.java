package dev.forge.requester.collection.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.collection.json.collection.Collection;
import dev.forge.requester.collection.json.collection.CollectionItem;
import dev.forge.requester.collection.utils.CollectionLoader;
import dev.forge.requester.collection.utils.CollectionPrinter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "list", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {
    @Parameters(index = "0", arity = "0..1", description = "list details about a specific item in the collection")
    String id;

    @Option(names = "--dir", required = false)
    String dir;

    private final ForgeConfigLoader conf = ForgeEngine.context().config();
    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        if (dir == null || dir.isBlank())
            dir = conf.getString("requester.collections.directory");

        Path collectionsDir = Paths.get(dir);
        CollectionLoader loader = new CollectionLoader(collectionsDir);
        loader.loadAll();
        List<Collection> collections = loader.getCollections();

        if (id != null && !id.isBlank()) {
            Object target = loader.getCollection(id);
            if (target != null) {
                CollectionPrinter.printCollection((Collection) target);
                return;
            }

            target = loader.getItem(id);
            if (target == null) {
                console.warn("no item found with id: " + id);
                return;
            }

            if (target instanceof CollectionItem.Folder folder)
                CollectionPrinter.printFolder(folder);
            else if (target instanceof CollectionItem.Request req)
                CollectionPrinter.printRequest(req);

        } else
            CollectionPrinter.printCollections(collections);
    }
}
