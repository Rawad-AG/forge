package dev.forge.nexo.utils;

import dev.forge.engine.utils.StringUtils;
import dev.forge.nexo.core.phases.modeler.models.field.AccessModifier;
import dev.forge.nexo.core.phases.modeler.models.field.FieldModel;
import dev.forge.nexo.core.phases.modeler.models.field.JavaTypeModel;
import dev.forge.nexo.core.phases.modeler.models.method.MethodBody;
import dev.forge.nexo.core.phases.modeler.models.method.MethodModel;
import dev.forge.nexo.core.phases.modeler.models.method.ParameterModel;

public class MethodsRepo {

    public static MethodModel toString(MethodBody body) {
        var method = new MethodModel("toString", "String");
        method.addAnnotation(AnnotationRepo.override());
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.setBody(body);
        return method;
    }

    public static MethodModel toString(String entity, String idField) {
        var body = new MethodBody();
        body.addStatement("return \"" + entity + " [id=\" + " + idField + " + \"]\"");
        return toString(body);
    }

    public static MethodModel hashCode(MethodBody body) {
        var method = new MethodModel("hashCode", "int");
        method.addAnnotation(AnnotationRepo.override());
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.setBody(body);
        return method;
    }

    public static MethodModel entityHashCode() {
        var body = new MethodBody();
        body.addStatement("return getClass().hashCode()");
        return hashCode(body);
    }

    public static MethodModel equals(MethodBody body) {
        var method = new MethodModel("equals", "boolean");
        method.addAnnotation(AnnotationRepo.override());
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.addParameter(new ParameterModel("obj", "Object"));
        method.setBody(body);
        return method;
    }

    public static MethodModel getter(String fieldName, JavaTypeModel fieldType) {
        if ("boolean".equals(fieldType.type()) || "Boolean".equals(fieldType.type()))
            return booleanGetter(fieldName);

        String methodName = "get" + StringUtils.capitalize(fieldName);
        var method = new MethodModel(methodName, fieldType.toString());
        method.setAccessModifier(AccessModifier.PUBLIC);
        var body = new MethodBody();
        body.addStatement("return " + fieldName);
        method.setBody(body);
        return method;
    }

    public static MethodModel booleanGetter(String fieldName) {
        String methodName = "is" + StringUtils.capitalize(fieldName);
        var method = new MethodModel(methodName, "boolean");
        method.setAccessModifier(AccessModifier.PUBLIC);
        var body = new MethodBody();
        body.addStatement("return " + fieldName);
        method.setBody(body);
        return method;
    }

    public static MethodModel setter(String fieldName, JavaTypeModel fieldType) {
        String methodName = "set" + StringUtils.capitalize(fieldName);
        var method = new MethodModel(methodName, "void");
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.addParameter(new ParameterModel(fieldName, fieldType.toString()));
        var body = new MethodBody();
        body.addStatement("this." + fieldName + " = " + fieldName);
        method.setBody(body);
        return method;
    }

    public static MethodModel emptyMethod(String name, String returnType) {
        var method = new MethodModel(name, returnType);
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.setBody(new MethodBody());
        return method;
    }

    public static MethodModel equals(String type, FieldModel targetField) {
        var body = new MethodBody();

        body.addStatement("if (this == obj) return true");
        body.addStatement("if (!(obj instanceof " + type + " other)) return false");
        body.addStatement("return "
                + targetField.getName()
                + " != null && "
                + targetField.getName()
                + ".equals(other."
                + targetField.getName()
                + ")");

        return equals(body);
    }

}
