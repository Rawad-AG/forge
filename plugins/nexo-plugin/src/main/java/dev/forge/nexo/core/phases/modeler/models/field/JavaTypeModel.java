package dev.forge.nexo.core.phases.modeler.models.field;

public record JavaTypeModel(
        String type,
        String generic) {

    @Override
    public String toString() {
        if (generic != null)
            return generic + "<" + type + ">";

        return type;
    }

}