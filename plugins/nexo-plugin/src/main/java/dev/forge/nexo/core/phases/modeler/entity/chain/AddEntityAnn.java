package dev.forge.nexo.core.phases.modeler.entity.chain;

import dev.forge.nexo.core.phases.modeler.entity.ChainElement;
import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;
import dev.forge.nexo.utils.AnnotationRepo;

public class AddEntityAnn implements ChainElement {

    @Override
    public void execute(ClassModel model, EntityDefinition entity) {
        model.addAnnotation(AnnotationRepo.entity(entity.name()));
    }
}
