package dev.forge.requester.collection.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fusesource.jansi.Ansi.Color;

import dev.forge.engine.cli.input.Prompter;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.collection.json.MetaData;
import dev.forge.requester.collection.json.collection.Collection;
import dev.forge.requester.collection.json.collection.CollectionItem;
import dev.forge.requester.collection.json.collection.RequestOptions;
import dev.forge.requester.http.HttpMethod;

public class CollectionEditor {
    private final Prompter prompter = ForgeEngine.context().prompter();

    public void editCollection(Collection collection, String dir) {
        var meta = updateMetaDate(collection.meta());
        CollectionUtils.writeCollection(new Collection(meta, collection.items()), dir);
    }

    public void editItem(Collection collection, CollectionItem item, String dir) {
        var meta = updateMetaDate(item.meta());
        if (item instanceof CollectionItem.Folder folder) {
            var newItems = collection.items().stream().map(f -> {
                if (f.meta().id().equals(folder.meta().id()))
                    return new CollectionItem.Folder(meta, ((CollectionItem.Folder) f).items());

                return f;
            }).toList();

            collection.items().clear();
            collection.items().addAll(newItems);

            CollectionUtils.writeCollection(collection, dir);

        }

        if (item instanceof CollectionItem.Request request) {
            var method = prompter.chooseOrDefault("method", HttpMethod.GET, HttpMethod.class);

            var reqOptions = new RequestOptions(
                    prompter.confirmOrDefault("enable", true),
                    prompter.askIntOrDefault("timeout in ms", request.options().timeoutMs()),
                    prompter.askIntOrDefault("retries", request.options().retries()));

            var newItems = collection.items().stream().map(f -> {
                if (f.meta().id().equals(request.meta().id()))
                    return new CollectionItem.Request(meta,
                            method,
                            prompter.askOrDefault("url", request.url()),
                            request.headers(),
                            request.body(),
                            reqOptions);

                return f;
            }).toList();

            collection.items().clear();
            collection.items().addAll(newItems);

            CollectionUtils.writeCollection(collection, dir);
        }

    }

    // ╔═════════════════════════════════════════════════════════════╗
    // ║ Internals
    // ╚═════════════════════════════════════════════════════════════╝
    private MetaData updateMetaDate(MetaData old) {
        List<String> colors = new ArrayList<>();
        Arrays.asList(Color.values()).forEach(m -> colors.add(m.toString()));

        return new MetaData(old.id(),
                prompter.askOrDefault("name", old.name()),
                prompter.askOrDefault("description", old.description()),
                prompter.chooseOrDefault("color", old.color(), Color.class),
                prompter.askOrDefault("icon", old.icon()));
    }

}
