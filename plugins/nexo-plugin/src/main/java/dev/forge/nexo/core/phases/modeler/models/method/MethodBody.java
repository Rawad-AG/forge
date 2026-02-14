package dev.forge.nexo.core.phases.modeler.models.method;

import java.util.ArrayList;
import java.util.List;

public class MethodBody {
    private final List<String> statements = new ArrayList<>();

    public void addStatement(String s) {
        statements.add(s);
    }

    @Override
    public String toString() {
        return "{" + String.join("; ", statements) + ";}";
    }

}
