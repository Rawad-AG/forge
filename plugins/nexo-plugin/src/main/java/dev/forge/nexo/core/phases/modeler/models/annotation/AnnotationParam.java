package dev.forge.nexo.core.phases.modeler.models.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AnnotationParam {
    private String key;
    private AnnotationValue value;

    @Override
    public String toString() {
        if (value == null)
            return key;

        return key + " = " + value;
    }

}
