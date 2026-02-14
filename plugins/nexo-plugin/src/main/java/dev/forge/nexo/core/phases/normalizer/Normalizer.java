package dev.forge.nexo.core.phases.normalizer;

import java.util.List;
import java.util.Objects;

import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.engine.utils.ProjectUtils;
import dev.forge.engine.utils.StringUtils;
import dev.forge.nexo.core.DataBox;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.parser.mapping.EntityDefinition;
import dev.forge.nexo.core.phases.parser.mapping.EnumDefinition;
import dev.forge.nexo.core.phases.parser.mapping.EnvConfig;
import dev.forge.nexo.core.phases.parser.mapping.FieldDefinition;
import dev.forge.nexo.core.phases.parser.mapping.FieldScope;
import dev.forge.nexo.core.phases.parser.mapping.FieldType;
import dev.forge.nexo.core.phases.parser.mapping.IndexDefinition;
import dev.forge.nexo.core.phases.parser.mapping.PersistenceConfig;
import dev.forge.nexo.core.phases.parser.mapping.RelationDefinition;
import dev.forge.nexo.core.phases.parser.mapping.RelationSide;
import dev.forge.nexo.core.phases.parser.mapping.Root;
import dev.forge.nexo.core.phases.parser.mapping.ValidationConfig;
import dev.forge.nexo.utils.RelationshipType;
import lombok.NonNull;

public class Normalizer implements Runnable {
    private final ForgeConfigLoader configurer = ForgeEngine.context().config();

    @Override
    public void run() {
        Root root = NexoContext.get(RegistryKey.Parsed_Root);

        Root newRoot = new Root(
                normalizeEnv(root.env()),
                normalizeEntities(root.entities()),
                normalizeRelations(root.relations()),
                normalizeEnums(root.enums()));

        NexoContext.put(RegistryKey.Parsed_Root, new DataBox(newRoot));
    }

    private EnvConfig normalizeEnv(EnvConfig env) {
        String defaultPkg = ProjectUtils.getRootPackage() + ".generated";
        if (env == null)
            return new EnvConfig(defaultPkg,
                    configurer.getBoolean("defaults.useLombok", true),
                    configurer.getBoolean("defaults.generateMappers", true),
                    configurer.getString("defaults.dtoSuffix", "DTO"),
                    configurer.getInt("defaults.javaVersion", 17));

        return new EnvConfig(
                Objects.requireNonNullElse(env.basePackage(), defaultPkg),
                Objects.requireNonNullElse(env.useLombok(), configurer.getBoolean("defaults.useLombok", true)),
                Objects.requireNonNullElse(env.generateMappers(),
                        configurer.getBoolean("defaults.generateMappers", true)),
                Objects.requireNonNullElse(env.dtoSuffix(), configurer.getString("defaults.dtoSuffix", "DTO")),
                Objects.requireNonNullElse(env.javaVersion(), configurer.getInt("defaults.javaVersion", 17)));
    }

    private List<EntityDefinition> normalizeEntities(List<EntityDefinition> entities) {
        if (entities == null)
            return List.of();

        return entities.stream().map(entity -> new EntityDefinition(
                Objects.requireNonNull(entity.name(), "Entity name cannot be null"),
                Objects.requireNonNullElse(entity.tableName(), StringUtils.camelToSnake(entity.name()) + "s"),
                normalizeFields(entity.fields()),
                normalizeInexes(entity.indexes()))).toList();
    }

    private List<IndexDefinition> normalizeInexes(List<IndexDefinition> indexes) {
        if (indexes == null)
            return List.of();

        return indexes.stream().map(i -> new IndexDefinition(
                Objects.requireNonNullElse(i.columnList(), List.of()),
                Objects.requireNonNullElse(i.unique(), false))

        ).toList();
    }

    private List<FieldDefinition> normalizeFields(List<FieldDefinition> fields) {
        if (fields == null)
            return List.of(generateIdField());

        if (fields.stream().filter(f -> f.primary() != null && f.primary()).findFirst().isEmpty())
            fields.addFirst(generateIdField());

        return fields.stream().map(f -> {
            FieldType resolvedType = Objects.requireNonNullElse(f.type(), FieldType.STRING);
            PersistenceConfig p = normalizePersistence(f, resolvedType);

            return new FieldDefinition(
                    Objects.requireNonNull(f.name(), "Field name cannot be null"),
                    resolvedType,
                    Objects.requireNonNullElse(f.primary(), false),
                    Objects.requireNonNullElse(f.scope(), List.of(FieldScope.values())),
                    p,
                    normalizeValidation(f.validation(), p));
        }).toList();
    }

    private FieldDefinition generateIdField() {
        return new FieldDefinition(
                "id",
                FieldType.LONG,
                true,
                List.of(FieldScope.values()),
                null,
                null);
    }

    private PersistenceConfig normalizePersistence(FieldDefinition f, FieldType type) {
        PersistenceConfig p = f.persistence();

        String resolvedColDef = (p != null && p.columnDefinition() != null)
                ? p.columnDefinition()
                : type.getColumnDefinition();

        if (p == null)
            return new PersistenceConfig(
                    StringUtils.camelToSnake(f.name()),
                    resolvedColDef,
                    true,
                    false,
                    getDefaultLength(type),
                    0,
                    0,
                    true,
                    true);

        return new PersistenceConfig(
                Objects.requireNonNullElse(p.columnName(), StringUtils.camelToSnake(f.name())),
                resolvedColDef,
                Objects.requireNonNullElse(p.nullable(), true),
                Objects.requireNonNullElse(p.unique(), false),
                p.length() == null ? getDefaultLength(type) : p.length(),
                Objects.requireNonNullElse(p.precision(), 0),
                Objects.requireNonNullElse(p.scale(), 0),
                Objects.requireNonNullElse(p.insertable(), true),
                Objects.requireNonNullElse(p.updatable(), true));
    }

    private Integer getDefaultLength(FieldType type) {
        return (type == FieldType.STRING) ? 255 : null;
    }

    private ValidationConfig normalizeValidation(ValidationConfig v, PersistenceConfig p) {
        if (v == null)
            return new ValidationConfig(!p.nullable(), null, null, null, null, null, null);

        return new ValidationConfig(
                !p.nullable() || Objects.requireNonNullElse(v.notNull(), false),
                v.notEmpty(),
                v.size(),
                v.pattern(),
                v.email(),
                v.min(),
                v.max());
    }

    private List<RelationDefinition> normalizeRelations(List<RelationDefinition> relations) {
        if (relations == null)
            return List.of();

        return relations.stream().map(r -> new RelationDefinition(
                Objects.requireNonNull(r.type(), "the type of relation is requeired"),
                Objects.requireNonNullElse(r.bidirectional(), false),
                normalizeRelationSide(r, r.from()),
                normalizeRelationSide(r, r.to()),
                normalizeJoinTable(r)))
                .toList();
    }

    private String normalizeJoinTable(RelationDefinition r) {
        if (r.table() == null && r.type() == RelationshipType.MANY_TO_MANY) {
            String from = r.from().entity().endsWith("s")
                    ? r.from().entity()
                    : r.from().entity() + "s";

            String to = r.to().entity().endsWith("s")
                    ? r.to().entity()
                    : r.to().entity() + "s";

            return from + "_" + to;
        }
        return r.table();
    }

    private RelationSide normalizeRelationSide(RelationDefinition r, @NonNull RelationSide s) {
        String entityName = s.entity().substring(0, 1).toLowerCase() + s.entity().substring(1);
        String defaultName = r.type() == RelationshipType.MANY_TO_MANY
                ? entityName + "s"
                : entityName;

        return new RelationSide(
                Objects.requireNonNull(s.entity(), "entity can not be null in the sides of the relation"),
                Objects.requireNonNull(s.name(), defaultName),
                Objects.requireNonNullElse(s.orphanRemoval(), false),
                Objects.requireNonNullElse(s.cascade(), List.of()));
    }

    private List<EnumDefinition> normalizeEnums(List<EnumDefinition> enums) {
        if (enums == null)
            return List.of();

        return enums.stream()
                .map(e -> new EnumDefinition(
                        Objects.requireNonNull(e.name(), "enum name is required"),
                        Objects.requireNonNullElse(e.values(), List.of())))
                .toList();
    }
}