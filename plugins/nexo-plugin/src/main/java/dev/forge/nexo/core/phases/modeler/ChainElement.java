package dev.forge.nexo.core.phases.modeler;

import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;

@FunctionalInterface
public interface ChainElement {
    void execute(ClassModel model, EntityDefinition entity);
}
