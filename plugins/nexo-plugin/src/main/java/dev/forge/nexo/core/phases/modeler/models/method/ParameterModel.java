package dev.forge.nexo.core.phases.modeler.models.method;

public record ParameterModel(
        String name,
        String type) {

    @Override
    public String toString() {
        return type + " " + name;
    }

}