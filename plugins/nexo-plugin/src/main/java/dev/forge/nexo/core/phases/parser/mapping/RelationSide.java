package dev.forge.nexo.core.phases.parser.mapping;

import java.util.List;

import dev.forge.nexo.utils.CascadeType;

public record RelationSide(
                String entity,
                String name,
                Boolean orphanRemoval,
                List<CascadeType> cascade) {
}