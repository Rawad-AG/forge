package dev.forge.nexo.core.phases.parser.mapping;

import java.util.List;

public record IndexDefinition(
        List<String> columnList,
        Boolean unique) {
}