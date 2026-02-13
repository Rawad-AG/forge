package dev.forge.nexo.core.phases.parser.mapping;

import java.util.List;

public record EnumDefinition(
        String name,
        List<String> values) {
}