package dev.forge.nexo.core.phases.modeler.models.method;

import java.util.ArrayList;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationModel;
import dev.forge.nexo.core.phases.modeler.models.field.AccessModifier;
import lombok.Data;

@Data
public class MethodModel {
    protected String name;
    protected String returnType;
    protected MethodBody body;
    protected boolean isStatic;
    protected boolean isFinal;
    protected boolean isAbstract;
    protected AccessModifier accessModifier = AccessModifier.PUBLIC;
    protected List<ParameterModel> parameters = new ArrayList<>();
    protected List<AnnotationModel> annotations = new ArrayList<>();

    public void setAbstract(boolean a) {
        if (a)
            body = null;

        isAbstract = a;
    }

    public MethodModel(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public void addParameter(ParameterModel param) {
        this.parameters.add(param);
    }

    public void addAnnotation(AnnotationModel annotation) {
        this.annotations.add(annotation);
    }

    @Override
    public String toString() {
        List<String> segmants = new ArrayList<>();
        segmants.add(accessModifier.getValue());
        if (isStatic)
            segmants.add("static");

        if (isFinal)
            segmants.add("final");

        if (isAbstract)
            segmants.add("abstract");

        segmants.add(returnType);

        segmants.add(name);

        segmants.add("(");
        segmants.add(String.join(", ", parameters.stream().map(ParameterModel::toString).toList()));

        segmants.add(")");

        if (isAbstract || body == null)
            return String.join(" ", segmants) + ";";

        segmants.add(body.toString());
        return String.join(" ", segmants);
    }

}
