package dev.forge.requester.collection.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.forge.requester.collection.json.collection.Collection;
import dev.forge.requester.collection.json.collection.CollectionItem;
import lombok.Getter;

@Getter
public final class CollectionLoader {

    private final Path root;
    private Map<String, Collection> collectionsMap = new HashMap<>();
    private Map<String, CollectionItem> items = new HashMap<>();
    private List<Collection> collections = new ArrayList<>();

    public CollectionLoader(Path root) {
        this.root = root;
    }

    public Collection getParent(CollectionItem item) {
        for (var c : collections)
            for (var i : c.items())
                if (i.meta().id().equals(item.meta().id()))
                    return c;

        throw new RuntimeException(
                "id can not be resolved, make sure you did not edit the collection.json file manually");
    }

    public Collection getCollection(String id) {
        return collectionsMap.get(id);
    }

    public CollectionItem getItem(String id) {
        return items.get(id);
    }

    public void loadAll() {
        if (!Files.exists(root) || !Files.isDirectory(root)) {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        try (Stream<Path> dirs = Files.list(root)) {
            dirs.sorted().filter(Files::isDirectory).forEach(dir -> {
                Path file = dir.resolve("collection.json");
                if (!Files.exists(file)) {
                    return;
                }

                try {
                    Collection c = mapper.readValue(file.toFile(), Collection.class);
                    collections.add(c);
                    collectionsMap.put(c.meta().id(), c);
                    c.items().forEach(i -> addItem(i));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load collection: " + file, e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read collections directory", e);
        }

    }

    private void addItem(CollectionItem i) {
        items.put(i.meta().id(), i);
        if (i instanceof CollectionItem.Folder folder)
            folder.items().forEach(f -> addItem(f));
    }

}
