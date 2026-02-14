package dev.forge.nexo.core.phases.modeler.models;

import java.util.ArrayList;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.method.MethodModel;

public class InterfaceModel extends JavaFileModel {
    private List<MethodModel> methods = new ArrayList<>();

    public InterfaceModel(String packageName, String className) {
        super(packageName, className);
    }

    public List<MethodModel> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodModel> methods) {
        this.methods = methods;
    }

    public void addMethod(MethodModel method) {
        this.methods.add(method);
    }
}
