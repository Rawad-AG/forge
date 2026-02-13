package dev.forge.nexo.core.phases.parser.mapping;

import dev.forge.nexo.utils.RelationshipType;

public record RelationDefinition(
        RelationshipType type,
        Boolean bidirectional,
        RelationSide from,
        RelationSide to,
        String table) {
}