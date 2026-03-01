package dev.forge.nexo.core.phases.modeler.entity.chain;

import dev.forge.nexo.core.phases.modeler.ChainElement;
import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.modeler.models.field.AccessModifier;
import dev.forge.nexo.core.phases.modeler.models.field.FieldModel;
import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;
import dev.forge.nexo.core.phases.parser.mapping.FieldType;
import dev.forge.nexo.utils.AnnotationRepo;

public class AddFields implements ChainElement {

    @Override
    public void execute(ClassModel model, EntityDefinition entity) {

        for (var f : entity.fields()) {
            var type = f.type() == FieldType.ENUM ? f.ref() : f.type().getJavaType();
            var field = new FieldModel(f.name(), type);
            field.setAccessModifier(AccessModifier.PRIVATE);

            if (f.primary())
                field.addAnnotation(AnnotationRepo.id());

            model.addField(field);
        }
    }
}
