package dev.forge.nexo.core.phases.modeler.models.field;

import lombok.Getter;

@Getter
public enum AccessModifier {
    PRIVATE("private"),
    PUBLIC("public"),
    PROTECTED("protected"),
    DEFAULT("");

    private String value;

    AccessModifier(String value) {
        this.value = value;
    }

}
