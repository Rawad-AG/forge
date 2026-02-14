package dev.forge.nexo.core.phases.modeler.entity.chain;

import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.modeler.entity.ChainElement;
import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;
import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.utils.AnnotationRepo;
import dev.forge.nexo.utils.MethodsRepo;

public class AddGetterSetter implements ChainElement {

    @Override
    public void execute(ClassModel model, EntityDefinition entity) {
        Root root = NexoContext.get(RegistryKey.Parsed_Root);

        if (root.env().useLombok()) {
            model.addAnnotation(AnnotationRepo.getter());
            model.addAnnotation(AnnotationRepo.setter());
            return;
        }

        for (var f : model.getFields()) {
            model.addMethod(MethodsRepo.getter(f.getName(), f.getType()));
            model.addMethod(MethodsRepo.setter(f.getName(), f.getType()));
        }
    }
}
