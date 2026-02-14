package dev.forge.nexo.core.phases.modeler.models;

import java.util.ArrayList;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.field.FieldModel;
import dev.forge.nexo.core.phases.modeler.models.method.ConstructorModel;
import dev.forge.nexo.core.phases.modeler.models.method.MethodModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassModel extends JavaFileModel {
    private List<FieldModel> fields = new ArrayList<>();
    private List<MethodModel> methods = new ArrayList<>();
    private List<ConstructorModel> constructors = new ArrayList<>();
    private String superclass;

    public ClassModel(String packageName, String className) {
        super(packageName, className);
    }

    public void addField(FieldModel field) {
        this.fields.add(field);
    }

    public void addMethod(MethodModel method) {
        this.methods.add(method);
    }

    public void addConstructor(ConstructorModel constructor) {
        this.constructors.add(constructor);
    }

}
