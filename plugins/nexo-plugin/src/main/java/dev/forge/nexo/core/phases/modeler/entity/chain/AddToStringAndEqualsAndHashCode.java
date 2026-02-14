package dev.forge.nexo.core.phases.modeler.entity.chain;

import dev.forge.nexo.core.phases.modeler.entity.ChainElement;
import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;
import dev.forge.nexo.utils.MethodsRepo;

public class AddToStringAndEqualsAndHashCode implements ChainElement {

    @Override
    public void execute(ClassModel model, EntityDefinition entity) {

        var primaryField = model.getFields().stream()
                .filter(f -> f.hasAnnotation("Id"))
                .findFirst()
                .get();

        if (primaryField != null) {
            model.addMethod(MethodsRepo.toString(entity.name(), primaryField.getName()));
            model.addMethod(MethodsRepo.hashCode(primaryField.getType()));
            model.addMethod(MethodsRepo.equals(entity.name(), primaryField));
        }
    }

}
