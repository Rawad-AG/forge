package dev.forge.requester.collection.commands;

import java.nio.file.Path;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.collection.json.collection.Collection;
import dev.forge.requester.collection.utils.CollectionEditor;
import dev.forge.requester.collection.utils.CollectionLoader;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "edit", mixinStandardHelpOptions = true)
public class EditCommand implements Runnable {
    @Parameters(index = "0", description = "the id of the item you want to edit")
    String itemId;

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

            Collection collection = null;
            var item = loader.getItem(itemId);
            var editor = new CollectionEditor();

            if (item == null)
                collection = loader.getCollection(itemId);
            else
                collection = loader.getParent(item);

            if (collection != null)
                editor.editCollection(collection, dir);

            else if (item != null)
                editor.editItem(collection, item, dir);

        } catch (Exception e) {
            console.error("Error:", e);
        }
    }

}
