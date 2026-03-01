package dev.forge.nexo.core.phases.parser.mapping;

import java.util.List;

public record FieldDefinition(
        String name,
        FieldType type,
        String ref,
        Boolean primary,
        List<FieldScope> scope,
        PersistenceConfig persistence,
        ValidationConfig validation,
        String defaultValue) {

}