package dev.forge.nexo.core.phases.model;

import java.util.List;

import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;

public record EntityModel(
                EntityDefinition definition,
                List<FieldModel> fields,
                List<RelationModel> relations,
                String tableName,
                String packageName,
                List<String> imports) {

    public record FieldModel(
                    String name,
                    String type,
                    String columnName,
                    boolean nullable,
                    boolean unique) {
    }

    public record RelationModel(
                    String name,
                    String type,
                    String targetEntity,
                    String mappedBy,
                    boolean orphanRemoval,
                    List<String> cascade) {
    }
}
