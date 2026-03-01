package dev.forge.nexo.core.phases.validator;

import java.util.List;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.core.phases.validator.rules.DuplicateNameRule;
import dev.forge.nexo.core.phases.validator.rules.EntityExistenceRule;
import dev.forge.nexo.core.phases.validator.rules.EnumExistenceRule;
import dev.forge.nexo.core.phases.validator.rules.IndexFieldsRule;
import dev.forge.nexo.core.phases.validator.rules.JoinTableRule;
import dev.forge.nexo.core.phases.validator.rules.PackageNameRule;
import dev.forge.nexo.core.phases.validator.rules.ReservedKeywordsRule;

public class Validator implements Runnable {
    private final Console console = ForgeEngine.context().console();

    private final List<Rule> rules = List.of(
            new PackageNameRule(),
            new DuplicateNameRule(),
            new ReservedKeywordsRule(),
            new EntityExistenceRule(),
            new EnumExistenceRule(),
            new IndexFieldsRule(),
            new JoinTableRule());

    @Override
    public void run() {
        Root root = NexoContext.get(RegistryKey.Parsed_Root);

        List<String> errors = rules.stream()
                .flatMap(rule -> rule.validate(root).stream())
                .toList();

        if (errors.isEmpty())
            return;

        errors.forEach(console::error);
        throw new RuntimeException("Semantic Validation Failed with " + errors.size() + " errors.");
    }
}