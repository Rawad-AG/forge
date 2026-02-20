package dev.forge.nexo.core.phases.modeler.models.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnumValue implements AnnotationValue {
    private final String enm;
    private final String value;

    @Override
    public String toString() {
        return enm + "." + value;
    }
}