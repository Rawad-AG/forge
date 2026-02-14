package dev.forge.nexo.core.phases.modeler.models.enums;

import java.util.ArrayList;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.JavaFileModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnumModel extends JavaFileModel {
    private List<EnumConstantModel> constants;

    public EnumModel(String packageName, String className) {
        super(packageName, className);
        this.constants = new ArrayList<>();
    }

    public void addConstant(EnumConstantModel constant) {
        this.constants.add(constant);
    }
}
