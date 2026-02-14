package dev.forge.nexo.core.phases.modeler;

import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.JavaFileModel;

public interface ModelBuilder {
    void build();

    List<JavaFileModel> getModels();
}
