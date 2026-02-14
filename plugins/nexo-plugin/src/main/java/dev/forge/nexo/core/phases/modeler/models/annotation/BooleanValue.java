package dev.forge.nexo.core.phases.modeler.models.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BooleanValue implements AnnotationValue {
    private final boolean value;

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}