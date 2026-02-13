package dev.forge.nexo.core.phases.validator.rules;

import java.util.ArrayList;
import java.util.List;

import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.core.phases.validator.Rule;
import dev.forge.nexo.utils.RelationshipType;

public class JoinTableRule implements Rule {

    @Override
    public List<String> validate(Root root) {
        List<String> errors = new ArrayList<>();

        for (var rel : root.relations()) {
            boolean hasJoinTable = rel.table() != null && !rel.table().isBlank();

            if (hasJoinTable && rel.type() != RelationshipType.MANY_TO_MANY)
                errors.add("Join table can only be specified for MANY_TO_MANY relationships. "
                        + "Found in '" + rel.from().entity() + " -> " + rel.to().entity() + "' (" + rel.type() + ").");

            if (!hasJoinTable && rel.type() == RelationshipType.MANY_TO_MANY)
                errors.add("MANY_TO_MANY relationship '" + rel.from().entity() + " -> " + rel.to().entity()
                        + "' requires a join table name.");
        }

        return errors;
    }
}
