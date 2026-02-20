package dev.forge.nexo.core.phases.modeler.entity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationParam;
import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationValue;
import dev.forge.nexo.core.phases.modeler.models.annotation.ArrayValue;
import dev.forge.nexo.core.phases.modeler.models.annotation.BooleanValue;
import dev.forge.nexo.core.phases.modeler.models.annotation.EnumValue;
import dev.forge.nexo.core.phases.modeler.models.field.FieldModel;
import dev.forge.nexo.core.phases.modeler.models.field.JavaTypeModel;
import dev.forge.nexo.core.phases.parser.mapping.RelationDefinition;
import dev.forge.nexo.core.phases.parser.mapping.RelationSide;
import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.utils.AnnotationRepo;
import dev.forge.nexo.utils.CascadeType;
import dev.forge.nexo.utils.RelationshipType;

public class RelationHandler {
    private static Root root;

    public static void handle(List<ClassModel> entities) {
        root = NexoContext.get(RegistryKey.Parsed_Root);

        if (root.relations() == null || root.relations().isEmpty())
            return;

        Map<String, ClassModel> entityMap = entities.stream()
                .collect(Collectors.toMap(ClassModel::getName, e -> e));

        for (RelationDefinition relation : root.relations())
            handleRelation(relation, entityMap);

    }

    private static void handleRelation(RelationDefinition relation, Map<String, ClassModel> entityMap) {
        RelationshipType type = relation.type();
        RelationSide from = relation.from();
        RelationSide to = relation.to();
        boolean bidirectional = relation.bidirectional();

        ClassModel sourceEntity = entityMap.get(from.entity());
        ClassModel targetEntity = entityMap.get(to.entity());

        if (sourceEntity == null || targetEntity == null)
            return;

        switch (type) {
            case ONE_TO_MANY -> handleOneToMany(relation, sourceEntity, targetEntity, bidirectional);
            case MANY_TO_ONE -> handleManyToOne(relation, sourceEntity, targetEntity, bidirectional);
            case ONE_TO_ONE -> handleOneToOne(relation, sourceEntity, targetEntity, bidirectional);
            case MANY_TO_MANY -> handleManyToMany(relation, sourceEntity, targetEntity, bidirectional);
        }
    }

    private static void handleOneToMany(RelationDefinition relation, ClassModel source, ClassModel target,
            boolean bidirectional) {
        RelationSide from = relation.from();
        RelationSide to = relation.to();

        FieldModel field = new FieldModel(from.name(),
                new JavaTypeModel(target.getName(), root.env().relationCollection()));

        initCollection(source, field, root.env().relationCollection());

        if (bidirectional)
            field.getAnnotations().add(AnnotationRepo.oneToMany(to.name()));
        else
            field.getAnnotations().add(AnnotationRepo.oneToMany(null));

        addCascadeAnnotation(field, from.cascade(), "OneToMany", from.orphanRemoval());

        source.addField(field);

        if (bidirectional)
            addSyncSetter(source, target, from.name(), to.name(),
                    root.env().relationCollection() + "<" + target.getName() + ">");
    }

    private static void handleManyToOne(RelationDefinition relation, ClassModel source, ClassModel target,
            boolean bidirectional) {
        RelationSide from = relation.from();
        RelationSide to = relation.to();

        FieldModel field = new FieldModel(from.name(), target.getName());
        field.getAnnotations().add(AnnotationRepo.manyToOne());

        addCascadeAnnotation(field, from.cascade(), "ManyToOne", from.orphanRemoval());

        source.addField(field);

        if (bidirectional)
            addSyncSetter(source, target, from.name(), to.name(), target.getName());

    }

    private static void handleOneToOne(RelationDefinition relation, ClassModel source, ClassModel target,
            boolean bidirectional) {
        RelationSide from = relation.from();
        RelationSide to = relation.to();

        FieldModel field = new FieldModel(from.name(), target.getName());

        if (bidirectional)
            field.getAnnotations().add(AnnotationRepo.oneToOne(to.name()));
        else
            field.getAnnotations().add(AnnotationRepo.oneToOne(null));

        addCascadeAnnotation(field, from.cascade(), "OneToOne", from.orphanRemoval());

        source.addField(field);

        if (bidirectional)
            addSyncSetter(source, target, from.name(), to.name(), target.getName());

    }

    private static void handleManyToMany(RelationDefinition relation, ClassModel source, ClassModel target,
            boolean bidirectional) {
        RelationSide from = relation.from();
        RelationSide to = relation.to();

        FieldModel field = new FieldModel(from.name(),
                new JavaTypeModel(target.getName(), root.env().relationCollection()));
        initCollection(source, field, root.env().relationCollection());

        if (bidirectional)
            field.getAnnotations().add(AnnotationRepo.manyToMany(to.name()));
        else
            field.getAnnotations().add(AnnotationRepo.manyToMany(null));

        addCascadeAnnotation(field, from.cascade(), "ManyToMany", from.orphanRemoval());

        if (!bidirectional && relation.table() != null) {
            String joinTable = relation.table();
            String sourceColumn = from.entity().toLowerCase() + "_id";
            String targetColumn = to.entity().toLowerCase() + "_id";
            field.getAnnotations().add(AnnotationRepo.joinTable(joinTable, sourceColumn, targetColumn));
        }

        source.addField(field);

        if (bidirectional)
            addSyncSetter(source, target, from.name(), to.name(), "Set<" + target.getName() + ">");

    }

    private static void addCascadeAnnotation(FieldModel field, List<CascadeType> cascadeTypes, String type,
            boolean orphan) {
        if (cascadeTypes == null || cascadeTypes.isEmpty())
            return;

        List<AnnotationValue> types = cascadeTypes.stream()
                .map(Enum::name)
                .map(t -> (AnnotationValue) new EnumValue("CascadeType", t))
                .toList();

        field.getAnnotations().stream()
                .filter(a -> a.getName().equals(type))
                .findAny()
                .ifPresent(a -> {
                    a.getParams().add(new AnnotationParam("cascade", new ArrayValue(types)));
                    if (orphan)
                        a.getParams().add(new AnnotationParam("orphanRemoval", new BooleanValue(true)));
                });
    }

    private static void addSyncSetter(ClassModel source, ClassModel target, String fieldName, String targetFieldName,
            String fieldType) {
        // TODO
    }

    private static void initCollection(ClassModel source, FieldModel field, String relationCollection) {
        if (relationCollection.equalsIgnoreCase("list"))
            field.setInitialization("new ArrayList<>()");
        else
            field.setInitialization("new HashSet<>()");
    }

}
