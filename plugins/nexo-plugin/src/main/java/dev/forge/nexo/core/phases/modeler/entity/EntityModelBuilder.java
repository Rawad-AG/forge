package dev.forge.nexo.core.phases.modeler.entity;

import java.util.ArrayList;
import java.util.List;

import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.modeler.ModelBuilder;
import dev.forge.nexo.core.phases.modeler.entity.chain.AddEntityAnn;
import dev.forge.nexo.core.phases.modeler.entity.chain.AddFields;
import dev.forge.nexo.core.phases.modeler.entity.chain.AddGetterSetter;
import dev.forge.nexo.core.phases.modeler.entity.chain.AddTableAnn;
import dev.forge.nexo.core.phases.modeler.entity.chain.AddToStringAndEqualsAndHashCode;
import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.modeler.models.JavaFileModel;
import dev.forge.nexo.core.phases.parser.mapping.Root;

public class EntityModelBuilder implements ModelBuilder {

    private List<JavaFileModel> entities = new ArrayList<>();
    private final ForgeConfigLoader configurer = ForgeEngine.context().config();
    private final List<ChainElement> chain = List.of(
            new AddTableAnn(),
            new AddEntityAnn(),
            new AddFields(),
            new AddGetterSetter(),
            new AddToStringAndEqualsAndHashCode()

    );

    @Override
    public void build() {
        Root root = NexoContext.get(RegistryKey.Parsed_Root);

        String basePackage = root.env().basePackage();

        String pkg = configurer.hasPath("defaults.packages.entity")
                ? basePackage + "." + configurer.getString("defaults.packages.entity")
                : basePackage;

        for (var entity : root.entities()) {
            ClassModel model = new ClassModel(pkg, entity.name());
            chain.forEach(c -> c.execute(model, entity));
            entities.add(model);
        }
    }

    @Override
    public List<JavaFileModel> getModels() {
        return entities;
    }
}
