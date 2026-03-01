package dev.forge.nexo.core.phases.modeler.models.method;

import java.util.ArrayList;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConstructorModel extends MethodModel {

    public ConstructorModel(String name) {
        super(name);
        this.body = new MethodBody();
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

        segmants.add(name);

        segmants.add("(");
        segmants.add(String.join(", ", parameters.stream().map(ParameterModel::toString).toList()));

        segmants.add(")");

        segmants.add("{\n");
        segmants.add(body.toString());
        segmants.add("\n}");

        return String.join(" ", segmants);
    }

}
