package dev.forge.nexo.core.phases.parser.mapping;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FieldType {
    STRING("String", null),
    INTEGER("Integer", null),
    LONG("Long", null),
    FLOAT("Float", null),
    DOUBLE("Double", null),
    BOOLEAN("Boolean", null),
    DECIMAL("BigDecimal", null),

    LOCAL_DATE("LocalDate", null),
    LOCAL_DATE_TIME("LocalDateTime", null),
    OFFSET_DATE_TIME("OffsetDateTime", null),
    ZONED_DATE_TIME("ZonedDateTime", null),
    LOCAL_TIME("LocalTime", null),

    UUID("UUID", "UUID"),
    BYTE_ARRAY("byte[]", "BLOB"),
    TEXT("String", "TEXT"),
    JSON("String", "JSONB"),
    ENUM("Enum", null);

    private final String javaType;
    private final String columnDefinition;

    FieldType(String javaType, String defaultColumnDefinition) {
        this.javaType = javaType;
        this.columnDefinition = defaultColumnDefinition;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getColumnDefinition() {
        return columnDefinition;
    }

    @JsonCreator
    public static FieldType fromString(String value) {
        if (value == null)
            return STRING;
        String normalized = value.trim().toUpperCase();
        for (FieldType type : FieldType.values()) {
            if (type.name().equals(normalized))
                return type;
        }
        throw new IllegalArgumentException("Unknown field type: " + value);
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}