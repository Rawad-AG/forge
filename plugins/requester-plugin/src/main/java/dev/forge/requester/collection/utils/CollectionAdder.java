package dev.forge.requester.collection.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import dev.forge.engine.cli.input.Prompter;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.collection.json.MetaData;
import dev.forge.requester.collection.json.collection.Collection;
import dev.forge.requester.collection.json.collection.CollectionItem;
import dev.forge.requester.collection.json.collection.RequestOptions;
import dev.forge.requester.http.HttpMethod;

public class CollectionAdder {
    private final Prompter prompter = ForgeEngine.context().prompter();

    private CollectionLoader loader;

    public CollectionAdder(CollectionLoader loader) {
        this.loader = loader;
    }

    public void addCollection(String dir) {
        Path root = Path.of(dir);
        if (!Files.exists(root) || !Files.isDirectory(root))
            try {
                Files.createDirectory(root);
            } catch (Exception e) {
                throw new RuntimeException("unable to create directory", e);
            }

        MetaData meta = askForMetaData();
        Collection collection = new Collection(meta, new ArrayList<>());

        try {
            Files.createDirectory(root.resolve(meta.name()));
            CollectionUtils.writeCollection(collection, dir);
        } catch (Exception e) {
            throw new RuntimeException("unable to create directory", e);
        }
    }

    public void addFolder(String dir) {
        var meta = askForMetaData();
        var folder = new CollectionItem.Folder(meta, new ArrayList<>());
        var collection = addItem(folder);

        try {
            CollectionUtils.writeCollection(collection, dir);
        } catch (Exception e) {
            throw new RuntimeException("unable to create directory", e);
        }
    }

    public void addRequest(String dir) {
        var meta = askForMetaData();
        var method = prompter.chooseOrDefault("method", HttpMethod.GET, HttpMethod.class);
        var url = prompter.askRequired("url");
        var request = new CollectionItem.Request(
                meta, method, url,
                new HashMap<>(), null,
                new RequestOptions(true, 10_000, 3));

        var collection = addItem(request);
        CollectionUtils.writeCollection(collection, dir);
    }

    // ╔═════════════════════════════════════════════════════════════╗
    // ║ Internals
    // ╚═════════════════════════════════════════════════════════════╝
    private MetaData askForMetaData() {
        String name = prompter.askRequired("name");
        String description = prompter.askOrDefault("description[optional]", "");
        return new MetaData(UUID.randomUUID().toString(), name, description, null, null);
    }

    private Collection addItem(CollectionItem target) {
        var collections = loader.getCollectionsMap();
        if (collections.isEmpty())
            throw new RuntimeException("no collections found");

        String id = collections.size() == 1
                ? collections.values().stream().findFirst().get().meta().id()
                : null;

        if (id == null)
            id = prompter.askRequired("choose a collection/folder to add to");
        else {
            String answer = prompter.askOrDefault("choose folder to add to", "collection");
            if (!answer.equals("collection"))
                id = answer;
        }

        Collection collection = collections.get(id);
        if (collection == null) {
            CollectionItem item = loader.getItem(id);

            if (item instanceof CollectionItem.Request)
                throw new RuntimeException("can not add items to request");

            ((CollectionItem.Folder) item).items().add(target);
            collection = loader.getParent(item);
        } else
            collection.items().add(target);

        return collection;
    }
}
