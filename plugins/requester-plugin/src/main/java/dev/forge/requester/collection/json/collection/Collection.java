package dev.forge.requester.collection.json.collection;

import java.util.List;

import dev.forge.requester.collection.json.MetaData;

public record Collection(
        MetaData meta,
        List<CollectionItem> items) {
}
