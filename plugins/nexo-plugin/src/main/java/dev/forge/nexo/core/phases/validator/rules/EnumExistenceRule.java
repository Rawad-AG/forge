package dev.forge.nexo.core.phases.validator.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.forge.nexo.core.phases.parser.mapping.EnumDefinition;
import dev.forge.nexo.core.phases.parser.mapping.FieldType;
import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.core.phases.validator.Rule;

public class EnumExistenceRule implements Rule {

    @Override
    public List<String> validate(Root root) {
        List<String> errors = new ArrayList<>();
        Set<String> enums = root.enums().stream()
                .map(EnumDefinition::name)
                .collect(Collectors.toSet());

        for (var e : root.entities()) {
            for (var f : e.fields()) {
                if (f.type() == FieldType.ENUM) {
                    if (f.ref() == null || f.ref().isBlank()) {
                        errors.add("Field error: the type Enum must define a ref attribute referencing the enum");
                        continue;
                    }
                    if (!enums.contains(f.ref()))
                        errors.add("Field error: Enum '" + f.ref() + "' does not exist.");

                    if (!enums.contains(f.ref()))
                        errors.add("Field error: Enum '" + f.ref() + "' does not exist.");
                }
            }

        }

        return errors;
    }
}