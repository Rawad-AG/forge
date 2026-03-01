package dev.forge.nexo.core.phases.modeler.models.method;

import dev.forge.nexo.core.phases.modeler.models.field.JavaTypeModel;

public record ParameterModel(
        String name,
        JavaTypeModel type) {

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }

}