package dev.forge.nexo.core.phases.modeler.models.annotation;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArrayValue implements AnnotationValue {
    private final List<AnnotationValue> values;

    @Override
    public String toString() {
        if (values == null || values.isEmpty())
            return "{}";

        var stringValues = values.stream().map(AnnotationValue::toString).toList();
        return "{ " + String.join(", ", stringValues) + " }";
    }

}
