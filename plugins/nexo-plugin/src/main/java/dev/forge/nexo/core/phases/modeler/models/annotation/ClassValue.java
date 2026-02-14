package dev.forge.nexo.core.phases.modeler.models.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClassValue implements AnnotationValue {
    private final Class<?> value;

    @Override
    public String toString() {
        return value + ".class";
    }
}