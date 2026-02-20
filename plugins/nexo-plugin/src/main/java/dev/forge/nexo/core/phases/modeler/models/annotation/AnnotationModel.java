package dev.forge.nexo.core.phases.modeler.models.annotation;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AnnotationModel {
    private String name;
    private List<AnnotationParam> params;

    public AnnotationModel(String name) {
        this.name = name;
        this.params = new ArrayList<>();
    }

    public AnnotationModel(String name, List<AnnotationParam> params) {
        this.name = name;
        this.params = params != null ? new ArrayList<>(params) : new ArrayList<>();
    }

    public boolean hasParams() {
        return params != null && !params.isEmpty();
    }

    @Override
    public String toString() {
        String definition = "@" + name;

        if (params == null || params.isEmpty())
            return definition;

        String paramsString = String.join(", ", params.stream().map(AnnotationParam::toString).toList());

        return definition + "(" + paramsString + ")";
    }

}
