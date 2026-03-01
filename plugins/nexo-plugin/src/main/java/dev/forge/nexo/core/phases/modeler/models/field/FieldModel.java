package dev.forge.nexo.core.phases.modeler.models.field;

import java.util.ArrayList;
import java.util.List;

import dev.forge.engine.utils.StringUtils;
import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldModel {
    private String name;
    private JavaTypeModel type;
    private AccessModifier accessModifier = AccessModifier.DEFAULT;
    private List<AnnotationModel> annotations = new ArrayList<>();
    private boolean isStatic;
    private boolean isFinal;
    private String initialization;

    public FieldModel(String name, JavaTypeModel type) {
        this.name = name;
        this.type = type;
    }

    public FieldModel(String name, String type) {
        this.name = name;
        this.type = new JavaTypeModel(type, null);
    }

    public void addAnnotation(AnnotationModel annotation) {
        this.annotations.add(annotation);
    }

    public String getGetterName() {
        if (type.type().equals("boolean")) {
            return "is" + StringUtils.capitalize(name);
        }
        return "get" + StringUtils.capitalize(name);
    }

    public String getSetterName() {
        return "set" + StringUtils.capitalize(name);
    }

    public boolean hasAnnotation(String name) {
        return annotations.stream()
                .filter(a -> a.getName().equals(name))
                .findAny()
                .isPresent();
    }

    public boolean isCollection() {
        return "set".equalsIgnoreCase(type.generic()) || "list".equalsIgnoreCase(type.generic());
    }

    @Override
    public String toString() {
        List<String> segmants = new ArrayList<>();
        segmants.add(accessModifier.getValue());
        if (isStatic)
            segmants.add("static");
        if (isFinal)
            segmants.add("final");
        segmants.add(type.toString());
        segmants.add(name);

        if (initialization != null && !initialization.isBlank()) {
            segmants.add("=");
            segmants.add(initialization);
        }

        return String.join(" ", segmants);
    }

}
