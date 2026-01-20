package dev.forge.requester.collection.json;

import org.fusesource.jansi.Ansi.Color;

public record MetaData(
        String id,
        String name,
        String description,
        Color color,
        String icon) {
}
