package dev.forge.nexo.utils;

import java.util.List;

import dev.forge.engine.utils.StringUtils;
import dev.forge.nexo.core.phases.modeler.models.field.AccessModifier;
import dev.forge.nexo.core.phases.modeler.models.field.FieldModel;
import dev.forge.nexo.core.phases.modeler.models.method.ConstructorModel;
import dev.forge.nexo.core.phases.modeler.models.method.MethodBody;
import dev.forge.nexo.core.phases.modeler.models.method.MethodModel;
import dev.forge.nexo.core.phases.modeler.models.method.ParameterModel;

public class MethodsRepo {

    public static MethodModel override(String name, String returnType) {
        var method = new MethodModel(name, returnType);
        method.addAnnotation(AnnotationRepo.override());
        method.setAccessModifier(AccessModifier.PUBLIC);
        return method;
    }

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

    public static MethodModel hashCode(String type) {
        var body = new MethodBody();
        body.addStatement("return " + type + ".class.hashCode()");
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

    public static MethodModel getter(String fieldName, String fieldType) {
        String methodName = "get" + StringUtils.capitalize(fieldName);
        var method = new MethodModel(methodName, fieldType);
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

    public static MethodModel setter(String fieldName, String fieldType) {
        String methodName = "set" + StringUtils.capitalize(fieldName);
        var method = new MethodModel(methodName, "void");
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.addParameter(new ParameterModel(fieldName, fieldType));
        var body = new MethodBody();
        body.addStatement("this." + fieldName + " = " + fieldName);
        method.setBody(body);
        return method;
    }

    public static MethodModel constructor(List<ParameterModel> parameters, MethodBody body) {
        var constructor = new ConstructorModel("constructor");
        constructor.setAccessModifier(AccessModifier.PUBLIC);
        for (var param : parameters) {
            constructor.addParameter(param);
        }
        constructor.setBody(body);
        return constructor;
    }

    public static MethodModel staticMethod(String name, String returnType, MethodBody body) {
        var method = new MethodModel(name, returnType);
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.setStatic(true);
        method.setBody(body);
        return method;
    }

    public static MethodModel abstractMethod(String name, String returnType) {
        var method = new MethodModel(name, returnType);
        method.setAccessModifier(AccessModifier.PUBLIC);
        return method;
    }

    public static MethodModel privateMethod(String name, String returnType, MethodBody body) {
        var method = new MethodModel(name, returnType);
        method.setAccessModifier(AccessModifier.PRIVATE);
        method.setBody(body);
        return method;
    }

    public static MethodModel protectedMethod(String name, String returnType, MethodBody body) {
        var method = new MethodModel(name, returnType);
        method.setAccessModifier(AccessModifier.PROTECTED);
        method.setBody(body);
        return method;
    }

    public static MethodModel builderMethod(String builderClassName) {
        var method = new MethodModel("builder", builderClassName);
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.setStatic(true);
        var body = new MethodBody();
        body.addStatement("return new " + builderClassName + "()");
        method.setBody(body);
        return method;
    }

    public static MethodModel builderSetter(String fieldName, String fieldType) {
        String methodName = StringUtils.capitalize(fieldName);
        var method = new MethodModel(methodName, "Builder");
        method.setAccessModifier(AccessModifier.PUBLIC);
        method.addParameter(new ParameterModel(fieldName, fieldType));
        var body = new MethodBody();
        body.addStatement("this." + fieldName + " = " + fieldName);
        body.addStatement("return this");
        method.setBody(body);
        return method;
    }

    public static MethodModel builderBuild(String returnType) {
        var method = new MethodModel("build", returnType);
        method.setAccessModifier(AccessModifier.PUBLIC);
        var body = new MethodBody();
        body.addStatement("return new " + returnType + "(this)");
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

        body.addStatement("if (!(obj instanceof " + type + ")) return false");

        body.addStatement(type + " other = (" + type + ") obj");

        body.addStatement("var otherId = " + "other." + targetField.getGetterName() + "()");
        body.addStatement("return otherId != null && this." + targetField.getName() + ".equals(otherId)");

        return equals(body);
    }

}
