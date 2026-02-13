package dev.forge.nexo.core.phases.parser.mapping;

import java.util.List;

public record Root(
                EnvConfig env,
                List<EntityDefinition> entities,
                List<RelationDefinition> relations,
                List<EnumDefinition> enums) {
}