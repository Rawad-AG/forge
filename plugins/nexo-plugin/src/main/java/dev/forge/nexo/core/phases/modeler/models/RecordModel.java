package dev.forge.nexo.core.phases.modeler.models;

import java.util.ArrayList;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.field.FieldModel;

public class RecordModel extends JavaFileModel {
    private List<FieldModel> components;

    public RecordModel(String packageName, String className) {
        super(packageName, className);
        this.components = new ArrayList<>();
    }

    public List<FieldModel> getComponents() {
        return components;
    }

    public void setComponents(List<FieldModel> components) {
        this.components = components;
    }

    public void addComponent(FieldModel component) {
        this.components.add(component);
    }
}
