package dev.forge.nexo.core.phases.validator.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.core.phases.validator.Rule;

public class PackageNameRule implements Rule {

    private static final Pattern VALID_PACKAGE_PATTERN = Pattern.compile("^[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)*$");

    @Override
    public List<String> validate(Root root) {
        List<String> errors = new ArrayList<>();

        String basePackage = root.env().basePackage();

        if (basePackage == null || basePackage.isBlank()) {
            errors.add("basePackage cannot be null or empty.");
            return errors;
        }

        if (!VALID_PACKAGE_PATTERN.matcher(basePackage).matches())
            errors.add("Invalid basePackage '" + basePackage
                    + "'. Must be lowercase letters, numbers, and dots (e.g., com.example.project).");

        return errors;
    }
}
