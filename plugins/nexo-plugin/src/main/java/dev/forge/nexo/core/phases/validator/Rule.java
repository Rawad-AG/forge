package dev.forge.nexo.core.phases.validator;

import java.util.List;

import dev.forge.nexo.core.phases.parser.mapping.Root;

public interface Rule {
    List<String> validate(Root root);
}
