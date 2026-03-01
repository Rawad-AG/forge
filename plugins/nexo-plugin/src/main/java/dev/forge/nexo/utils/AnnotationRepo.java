package dev.forge.nexo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationModel;
import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationParam;
import dev.forge.nexo.core.phases.modeler.models.annotation.AnnotationValue;
import dev.forge.nexo.core.phases.modeler.models.annotation.ArrayValue;
import dev.forge.nexo.core.phases.modeler.models.annotation.BooleanValue;
import dev.forge.nexo.core.phases.modeler.models.annotation.NestedAnnotationValue;
import dev.forge.nexo.core.phases.modeler.models.annotation.NumberValue;
import dev.forge.nexo.core.phases.modeler.models.annotation.StringValue;

public class AnnotationRepo {

    public static AnnotationModel entity() {
        return new AnnotationModel("Entity");
    }

    public static AnnotationModel entity(String name) {
        return new AnnotationModel("Entity", List.of(
                new AnnotationParam("name", new StringValue(name))));
    }

    public static AnnotationModel table(String name) {
        return new AnnotationModel("Table", List.of(
                new AnnotationParam("name", new StringValue(name))));
    }

    public static AnnotationModel table(String name, List<AnnotationValue> indexes,
            List<AnnotationValue> uniqueConstraints) {
        var params = new ArrayList<AnnotationParam>();
        params.add(new AnnotationParam("name", new StringValue(name)));

        if (indexes != null && !indexes.isEmpty()) {
            params.add(new AnnotationParam("indexes", new ArrayValue(indexes)));
        }

        if (uniqueConstraints != null && !uniqueConstraints.isEmpty()) {
            params.add(new AnnotationParam("uniqueConstraints", new ArrayValue(uniqueConstraints)));
        }

        return new AnnotationModel("Table", params);
    }

    public static AnnotationModel index(String name, String columnList) {
        return new AnnotationModel("Index", List.of(
                new AnnotationParam("name", new StringValue(name)),
                new AnnotationParam("columnList", new StringValue(columnList))));
    }

    public static AnnotationValue indexValue(String name, String columnList) {
        return new NestedAnnotationValue(index(name, columnList));
    }

    public static AnnotationModel uniqueConstraint(String name, List<String> columnNames) {
        List<AnnotationValue> values = columnNames.stream()
                .map(StringValue::new)
                .map(v -> (AnnotationValue) v)
                .toList();

        return new AnnotationModel("UniqueConstraint", List.of(
                new AnnotationParam("name", new StringValue(name)),
                new AnnotationParam("columnNames", new ArrayValue(values))));
    }

    public static AnnotationValue uniqueConstraintValue(String name, List<String> columnNames) {
        return new NestedAnnotationValue(uniqueConstraint(name, columnNames));
    }

    public static AnnotationModel column() {
        return new AnnotationModel("Column");
    }

    public static AnnotationModel column(String columnName) {
        return new AnnotationModel("Column", List.of(
                new AnnotationParam("name", new StringValue(columnName))));
    }

    public static AnnotationModel column(String columnName, Boolean nullable, Boolean unique, Integer length) {
        var params = new ArrayList<AnnotationParam>();

        if (columnName != null) {
            params.add(new AnnotationParam("name", new StringValue(columnName)));
        }
        if (nullable != null) {
            params.add(new AnnotationParam("nullable", new BooleanValue(nullable)));
        }
        if (unique != null) {
            params.add(new AnnotationParam("unique", new BooleanValue(unique)));
        }
        if (length != null) {
            params.add(new AnnotationParam("length", new NumberValue(length)));
        }

        return new AnnotationModel("Column", params);
    }

    public static AnnotationModel id() {
        return new AnnotationModel("Id");
    }

    public static AnnotationModel generatedValue() {
        return new AnnotationModel("GeneratedValue");
    }

    public static AnnotationModel sequenceGenerator(String name, String sequenceName) {
        return new AnnotationModel("SequenceGenerator", List.of(
                new AnnotationParam("name", new StringValue(name)),
                new AnnotationParam("sequenceName", new StringValue(sequenceName))));
    }

    public static AnnotationModel data() {
        return new AnnotationModel("Data");
    }

    public static AnnotationModel getter() {
        return new AnnotationModel("Getter");
    }

    public static AnnotationModel setter() {
        return new AnnotationModel("Setter");
    }

    public static AnnotationModel toStringAnnotation() {
        return new AnnotationModel("ToString");
    }

    public static AnnotationModel toStringInclude() {
        return new AnnotationModel("ToString", List.of(
                new AnnotationParam("onlyExplicitlyIncluded", new BooleanValue(true))));
    }

    public static AnnotationModel equalsAndHashCode() {
        return new AnnotationModel("EqualsAndHashCode");
    }

    public static AnnotationModel equalsAndHashCodeInclude() {
        return new AnnotationModel("EqualsAndHashCode", List.of(
                new AnnotationParam("onlyExplicitlyIncluded", new BooleanValue(true))));
    }

    public static AnnotationModel override() {
        return new AnnotationModel("Override");
    }

    public static AnnotationModel notNull() {
        return new AnnotationModel("NotNull");
    }

    public static AnnotationModel notBlank() {
        return new AnnotationModel("NotBlank");
    }

    public static AnnotationModel size(int min, int max) {
        return new AnnotationModel("Size", List.of(
                new AnnotationParam("min", new NumberValue(min)),
                new AnnotationParam("max", new NumberValue(max))));
    }

    public static AnnotationModel email() {
        return new AnnotationModel("Email");
    }

    public static AnnotationModel pattern(String regex) {
        return new AnnotationModel("Pattern", List.of(
                new AnnotationParam("regexp", new StringValue(regex))));
    }

    public static AnnotationModel builder() {
        return new AnnotationModel("Builder");
    }

    public static AnnotationModel builder(String builderClassName) {
        return new AnnotationModel("Builder", List.of(
                new AnnotationParam("builderClassName", new StringValue(builderClassName))));
    }

    public static AnnotationModel oneToOne(String mappedBy) {
        List<AnnotationParam> params = mappedBy == null ? List.of()
                : List.of(
                        new AnnotationParam("mappedBy", new StringValue(mappedBy)));
        return new AnnotationModel("OneToOne", params);
    }

    public static AnnotationModel oneToMany(String mappedBy) {
        List<AnnotationParam> params = mappedBy == null ? List.of()
                : List.of(
                        new AnnotationParam("mappedBy", new StringValue(mappedBy)));
        return new AnnotationModel("OneToMany", params);
    }

    public static AnnotationModel manyToOne() {
        return new AnnotationModel("ManyToOne");
    }

    public static AnnotationModel manyToMany(String mappedBy) {
        List<AnnotationParam> params = mappedBy == null ? List.of()
                : List.of(
                        new AnnotationParam("mappedBy", new StringValue(mappedBy)));
        return new AnnotationModel("ManyToMany", params);
    }

    public static AnnotationModel joinTable(String name, String joinColumns, String inverseJoinColumns) {
        var params = new ArrayList<AnnotationParam>();
        params.add(new AnnotationParam("name", new StringValue(name)));
        params.add(new AnnotationParam("joinColumns",
                new NestedAnnotationValue(new AnnotationModel("JoinColumn", List.of(
                        new AnnotationParam("name", new StringValue(joinColumns)))))));
        params.add(new AnnotationParam("inverseJoinColumns",
                new NestedAnnotationValue(new AnnotationModel("JoinColumn", List.of(
                        new AnnotationParam("name", new StringValue(inverseJoinColumns)))))));

        return new AnnotationModel("JoinTable", params);
    }

    public static AnnotationModel joinColumn(String name) {
        return new AnnotationModel("JoinColumn", List.of(
                new AnnotationParam("name", new StringValue(name))));
    }

    public static AnnotationModel cascade(String... types) {
        List<AnnotationValue> values = Arrays.stream(types)
                .map(s -> (AnnotationValue) new StringValue(s))
                .toList();

        return new AnnotationModel("Cascade", List.of(
                new AnnotationParam("value", new ArrayValue(values))));
    }

    public static AnnotationModel orphanRemoval() {
        return new AnnotationModel("OrphanRemoval", List.of(
                new AnnotationParam("value", new BooleanValue(true))));
    }

    public static AnnotationModel fetch(int value) {
        return new AnnotationModel("Fetch", List.of(
                new AnnotationParam("value", new NumberValue(value))));
    }

    public static AnnotationModel transient_() {
        return new AnnotationModel("Transient");
    }
}
