package dev.forge.requester.collection.json.env;

import dev.forge.requester.collection.json.MetaData;

public record EnvironmentValue(
        MetaData meta,
        String key,
        String value,
        boolean enabled) {
}