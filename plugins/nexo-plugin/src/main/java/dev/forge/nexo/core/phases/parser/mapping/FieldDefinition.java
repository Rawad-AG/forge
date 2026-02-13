package dev.forge.nexo.core.phases.parser.mapping;

import java.util.List;

public record FieldDefinition(
                String name,
                FieldType type,
                List<FieldScope> scope,
                PersistenceConfig persistence,
                ValidationConfig validation) {

}