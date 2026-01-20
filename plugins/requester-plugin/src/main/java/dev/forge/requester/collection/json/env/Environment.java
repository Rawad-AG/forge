package dev.forge.requester.collection.json.env;

import java.util.List;

import dev.forge.requester.collection.json.MetaData;

public record Environment(
        MetaData meta,
        List<EnvironmentValue> values) {
}