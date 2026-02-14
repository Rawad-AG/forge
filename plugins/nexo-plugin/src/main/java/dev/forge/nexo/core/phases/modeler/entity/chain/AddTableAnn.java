package dev.forge.nexo.core.phases.modeler.entity.chain;

import dev.forge.nexo.core.phases.modeler.entity.ChainElement;
import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationValue;
import dev.forge.nexo.core.phases.modeler.models.annotation.NestedAnnotationValue;
import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;
import dev.forge.nexo.core.phases.parser.mapping.IndexDefinition;
import dev.forge.nexo.utils.AnnotationRepo;

public class AddTableAnn implements ChainElement {

    @Override
    public void execute(ClassModel model, EntityDefinition entity) {
        var indexes = entity.indexes().stream()
                .filter(p -> !p.unique())
                .map(this::buildIndexAnnotation)
                .toList();

        var uniques = entity.indexes().stream()
                .filter(p -> p.unique())
                .map(this::buildUniqueAnnotation)
                .toList();

        model.addAnnotation(AnnotationRepo.table(entity.tableName(), indexes, uniques));
    }

    private AnnotationValue buildIndexAnnotation(IndexDefinition i) {
        String name = "idx_" + String.join("_", i.columnList());
        return new NestedAnnotationValue(AnnotationRepo.index(name, String.join(", ", i.columnList())));
    }

    private AnnotationValue buildUniqueAnnotation(IndexDefinition i) {
        String name = "uk_" + String.join("_", i.columnList());
        return new NestedAnnotationValue(AnnotationRepo.uniqueConstraint(name, i.columnList()));
    }
}
