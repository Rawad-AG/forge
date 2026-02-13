package dev.forge.nexo.core.phases.validator.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.core.phases.validator.Rule;

public class ReservedKeywordsRule implements Rule {

    private static final Set<String> JAVA_KEYWORDS = Set.of(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "final", "finally", "float", "for",
            "goto", "if", "implements", "import", "instanceof", "int",
            "interface", "long", "native", "new", "package", "private",
            "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "true", "false",
            "null");

    @Override
    public List<String> validate(Root root) {
        List<String> errors = new ArrayList<>();

        for (var entity : root.entities()) {
            if (JAVA_KEYWORDS.contains(entity.name().toLowerCase()))
                errors.add("Entity '" + entity.name() + "' uses a Java reserved keyword.");

            for (var field : entity.fields()) {
                if (JAVA_KEYWORDS.contains(field.name().toLowerCase()))
                    errors.add("Field '" + field.name() + "' in entity '" + entity.name()
                            + "' uses a Java reserved keyword.");
            }
        }

        return errors;
    }
}
