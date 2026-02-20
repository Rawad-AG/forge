package dev.forge.nexo.core.phases.modeler.models;

import java.util.ArrayList;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationModel;
import dev.forge.nexo.utils.ImportsRepo;
import lombok.Data;

@Data
public abstract class JavaFileModel {
    private String pkg;
    private String name;
    private List<String> imports = new ArrayList<>();
    private List<AnnotationModel> annotations = new ArrayList<>();
    private List<String> implementedInterfaces = new ArrayList<>();

    public JavaFileModel(String pkg, String name) {
        this.pkg = pkg;
        this.name = name;

        ImportsRepo.register(name, pkg + "." + name);
    }

    public void addAnnotation(AnnotationModel ann) {
        annotations.add(ann);
    }
}
