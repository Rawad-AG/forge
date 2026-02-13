package dev.forge.nexo.core.phases.validator.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;
import dev.forge.nexo.core.phases.parser.mapping.RelationDefinition;
import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.core.phases.validator.Rule;

public class EntityExistenceRule implements Rule {

    @Override
    public List<String> validate(Root root) {
        List<String> errors = new ArrayList<>();
        Set<String> entityNames = root.entities().stream()
                .map(EntityDefinition::name)
                .collect(Collectors.toSet());

        for (RelationDefinition rel : root.relations()) {
            if (!entityNames.contains(rel.from().entity()))
                errors.add("Relation error: Entity '" + rel.from().entity() + "' does not exist.");

            if (!entityNames.contains(rel.to().entity()))
                errors.add("Relation error: Entity '" + rel.to().entity() + "' does not exist.");
        }

        return errors;
    }
}