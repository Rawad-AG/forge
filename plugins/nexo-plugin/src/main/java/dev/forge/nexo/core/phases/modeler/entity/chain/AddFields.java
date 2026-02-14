package dev.forge.nexo.core.phases.modeler.entity.chain;

import dev.forge.nexo.core.phases.modeler.entity.ChainElement;
import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.modeler.models.field.AccessModifier;
import dev.forge.nexo.core.phases.modeler.models.field.FieldModel;
import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;
import dev.forge.nexo.utils.AnnotationRepo;

public class AddFields implements ChainElement {

    @Override
    public void execute(ClassModel model, EntityDefinition entity) {

        for (var f : entity.fields()) {
            var field = new FieldModel(f.name(), f.type().getJavaType());
            field.setAccessModifier(AccessModifier.PRIVATE);

            if (f.primary())
                field.addAnnotation(AnnotationRepo.id());

            model.addField(field);
        }
    }
}
