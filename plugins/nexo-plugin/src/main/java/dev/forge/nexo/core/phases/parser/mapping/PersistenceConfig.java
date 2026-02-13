package dev.forge.nexo.core.phases.parser.mapping;

public record PersistenceConfig(
                String columnName,
                String columnDefinition,
                Boolean nullable,
                Boolean unique,
                Integer length,
                Integer precision,
                Integer scale,
                Boolean insertable,
                Boolean updatable) {
}