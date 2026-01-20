package dev.forge.requester.collection.utils;

import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.forge.requester.collection.json.collection.Collection;

public class CollectionUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeCollection(Collection collection, String dir) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(Path.of(dir).resolve(collection.meta().name()).resolve("collection.json").toFile(),
                            collection);
        } catch (Exception e) {
            throw new RuntimeException("unable to create directory", e);
        }

    }
}