package dev.forge.nexo.core.phases.validator.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;
import dev.forge.nexo.core.phases.parser.mapping.FieldDefinition;
import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.core.phases.validator.Rule;

public class IndexFieldsRule implements Rule {

    @Override
    public List<String> validate(Root root) {
        List<String> errors = new ArrayList<>();

        for (EntityDefinition entity : root.entities()) {
            Set<String> fieldNames = entity.fields().stream()
                    .map(FieldDefinition::name)
                    .collect(Collectors.toSet());

            for (var index : entity.indexes()) {
                if (index.columnList() == null || index.columnList().isEmpty()) {
                    errors.add("Index in entity '" + entity.name() + "' has no columns defined.");
                    continue;
                }

                for (String column : index.columnList()) {
                    if (!fieldNames.contains(column))
                        errors.add("Index in entity '" + entity.name() + "' references non-existent field '" + column
                                + "'.");
                }
            }
        }

        return errors;
    }
}
