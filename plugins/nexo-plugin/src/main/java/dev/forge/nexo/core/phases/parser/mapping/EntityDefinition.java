package dev.forge.nexo.core.phases.parser.mapping;

import java.util.List;

public record EntityDefinition(
        String name,
        String tableName,
        List<FieldDefinition> fields,
        List<IndexDefinition> indexes) {
}