package dev.forge.nexo.core.phases.modeler.models.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NumberValue implements AnnotationValue {
    private final Number value;
}
