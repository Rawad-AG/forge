package dev.forge.nexo.core.phases.modeler.models.enums;

import java.util.ArrayList;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationModel;
import lombok.Data;

@Data
public class EnumConstantModel {
    private String name;
    private List<AnnotationModel> annotations;

    public EnumConstantModel(String name) {
        this.name = name;
        this.annotations = new ArrayList<>();
    }

    public void addAnnotation(AnnotationModel annotation) {
        this.annotations.add(annotation);
    }
}