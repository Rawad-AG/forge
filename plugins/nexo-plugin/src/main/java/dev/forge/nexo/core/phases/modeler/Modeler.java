package dev.forge.nexo.core.phases.modeler;

import java.util.List;

import dev.forge.nexo.core.DataBox;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.modeler.entity.EntityModelBuilder;
import dev.forge.nexo.core.phases.modeler.models.JavaFileModel;

public class Modeler implements Runnable {

    private final EntityModelBuilder entityModelBuilder = new EntityModelBuilder();

    @Override
    public void run() {
        entityModelBuilder.build();

        List<JavaFileModel> models = entityModelBuilder.getModels();
        NexoContext.put(RegistryKey.Models, new DataBox(models));
    }
}
