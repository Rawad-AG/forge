package dev.forge.nexo.core.phases.modeler.models.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NestedAnnotationValue implements AnnotationValue {
    private final AnnotationModel annotation;

    @Override
    public String toString() {
        return annotation.toString();
    }
}