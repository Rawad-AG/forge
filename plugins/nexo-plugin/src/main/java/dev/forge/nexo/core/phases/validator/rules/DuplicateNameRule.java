package dev.forge.nexo.core.phases.validator.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.core.phases.validator.Rule;

public class DuplicateNameRule implements Rule {

    @Override
    public List<String> validate(Root root) {
        List<String> errors = new ArrayList<>();
        Set<String> entityNames = new HashSet<>();

        for (var entity : root.entities()) {
            if (!entityNames.add(entity.name()))
                errors.add("Duplicate Entity name found: " + entity.name());

            Set<String> fieldNames = new HashSet<>();
            for (var field : entity.fields()) {
                if (!fieldNames.add(field.name()))
                    errors.add("Entity '" + entity.name() + "' has duplicate field: " + field.name());
            }
        }

        return errors;
    }
}