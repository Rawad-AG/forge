package dev.forge.nexo.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImportsRepo {
    private static final Map<String, String> imports = new ConcurrentHashMap<>();

    static {
        // Persistence / JPA
        register("Entity", "jakarta.persistence.Entity");
        register("Table", "jakarta.persistence.Table");
        register("Column", "jakarta.persistence.Column");
        register("Id", "jakarta.persistence.Id");
        register("GeneratedValue", "jakarta.persistence.GeneratedValue");
        register("SequenceGenerator", "jakarta.persistence.SequenceGenerator");
        register("TableGenerator", "jakarta.persistence.TableGenerator");
        register("OneToOne", "jakarta.persistence.OneToOne");
        register("OneToMany", "jakarta.persistence.OneToMany");
        register("ManyToOne", "jakarta.persistence.ManyToOne");
        register("ManyToMany", "jakarta.persistence.ManyToMany");
        register("JoinTable", "jakarta.persistence.JoinTable");
        register("JoinColumn", "jakarta.persistence.JoinColumn");
        register("PrimaryKeyJoinColumn", "jakarta.persistence.PrimaryKeyJoinColumn");
        register("ForeignKey", "jakarta.persistence.ForeignKey");
        register("Index", "jakarta.persistence.Index");
        register("UniqueConstraint", "jakarta.persistence.UniqueConstraint");
        register("NamedQuery", "jakarta.persistence.NamedQuery");
        register("NamedQueries", "jakarta.persistence.NamedQueries");
        register("SqlResultSetMapping", "jakarta.persistence.SqlResultSetMapping");
        register("NamedEntityGraph", "jakarta.persistence.NamedEntityGraph");
        register("EntityGraph", "jakarta.persistence.EntityGraph");
        register("Cacheable", "jakarta.persistence.Cacheable");
        register("Inheritance", "jakarta.persistence.Inheritance");
        register("DiscriminatorColumn", "jakarta.persistence.DiscriminatorColumn");
        register("DiscriminatorValue", "jakarta.persistence.DiscriminatorValue");
        register("MappedSuperclass", "jakarta.persistence.MappedSuperclass");
        register("Embeddable", "jakarta.persistence.Embeddable");
        register("Embedded", "jakarta.persistence.Embedded");
        register("EmbeddedId", "jakarta.persistence.EmbeddedId");
        register("AttributeOverride", "jakarta.persistence.AttributeOverride");
        register("AttributeOverrides", "jakarta.persistence.AttributeOverrides");
        register("AssociationOverride", "jakarta.persistence.AssociationOverride");
        register("AssociationOverrides", "jakarta.persistence.AssociationOverrides");
        register("Transient", "jakarta.persistence.Transient");
        register("Temporal", "jakarta.persistence.Temporal");
        register("Enumerated", "jakarta.persistence.Enumerated");
        register("Lob", "jakarta.persistence.Lob");
        register("Access", "jakarta.persistence.Access");
        register("OrderBy", "jakarta.persistence.OrderBy");
        register("OrderColumn", "jakarta.persistence.OrderColumn");
        register("MapKey", "jakarta.persistence.MapKey");

        // Java Util & Collections
        register("List", "java.util.List");
        register("Set", "java.util.Set");
        register("Map", "java.util.Map");
        register("Collection", "java.util.Collection");
        register("ArrayList", "java.util.ArrayList");
        register("HashSet", "java.util.HashSet");
        register("HashMap", "java.util.HashMap");
        register("Optional", "java.util.Optional");
        register("UUID", "java.util.UUID");
        register("Objects", "java.util.Objects");
        register("Arrays", "java.util.Arrays");
        register("Collections", "java.util.Collections");
        register("Stream", "java.util.stream.Stream");
        register("Serializable", "java.io.Serializable");

        // Math & Time
        register("BigDecimal", "java.math.BigDecimal");
        register("BigInteger", "java.math.BigInteger");
        register("LocalDate", "java.time.LocalDate");
        register("LocalTime", "java.time.LocalTime");
        register("LocalDateTime", "java.time.LocalDateTime");
        register("ZonedDateTime", "java.time.ZonedDateTime");
        register("OffsetDateTime", "java.time.OffsetDateTime");
        register("Instant", "java.time.Instant");
        register("Duration", "java.time.Duration");
        register("Period", "java.time.Period");
        register("DateTimeFormatter", "java.time.format.DateTimeFormatter");

        // Lombok
        register("Data", "lombok.Data");
        register("Getter", "lombok.Getter");
        register("Setter", "lombok.Setter");
        register("ToString", "lombok.ToString");
        register("EqualsAndHashCode", "lombok.EqualsAndHashCode");
        register("AllArgsConstructor", "lombok.AllArgsConstructor");
        register("NoArgsConstructor", "lombok.NoArgsConstructor");
        register("RequiredArgsConstructor", "lombok.RequiredArgsConstructor");
        register("Builder", "lombok.Builder");
        register("Value", "lombok.Value");
        register("With", "lombok.With");
        register("Slf4j", "lombok.extern.slf4j.Slf4j");

        // Validation
        register("NotNull", "jakarta.validation.constraints.NotNull");
        register("NotBlank", "jakarta.validation.constraints.NotBlank");
        register("NotEmpty", "jakarta.validation.constraints.NotEmpty");
        register("Size", "jakarta.validation.constraints.Size");
        register("Min", "jakarta.validation.constraints.Min");
        register("Max", "jakarta.validation.constraints.Max");
        register("Pattern", "jakarta.validation.constraints.Pattern");
        register("Email", "jakarta.validation.constraints.Email");
        register("Positive", "jakarta.validation.constraints.Positive");
        register("Past", "jakarta.validation.constraints.Past");
        register("Future", "jakarta.validation.constraints.Future");
        register("Valid", "jakarta.validation.Valid");

        // Spring Web / Annotations
        register("RestController", "org.springframework.web.bind.annotation.RestController");
        register("GetMapping", "org.springframework.web.bind.annotation.GetMapping");
        register("PostMapping", "org.springframework.web.bind.annotation.PostMapping");
        register("PutMapping", "org.springframework.web.bind.annotation.PutMapping");
        register("DeleteMapping", "org.springframework.web.bind.annotation.DeleteMapping");
        register("RequestBody", "org.springframework.web.bind.annotation.RequestBody");
        register("PathVariable", "org.springframework.web.bind.annotation.PathVariable");
        register("RequestParam", "org.springframework.web.bind.annotation.RequestParam");
        register("Service", "org.springframework.stereotype.Service");
        register("Repository", "org.springframework.stereotype.Repository");
        register("Component", "org.springframework.stereotype.Component");
        register("Configuration", "org.springframework.context.annotation.Configuration");
        register("Bean", "org.springframework.context.annotation.Bean");
        register("Autowired", "org.springframework.beans.factory.annotation.Autowired");
        register("Transactional", "org.springframework.transaction.annotation.Transactional");

        // Spring Data
        register("JpaRepository", "org.springframework.data.jpa.repository.JpaRepository");
        register("CrudRepository", "org.springframework.data.repository.CrudRepository");
        register("Page", "org.springframework.data.domain.Page");
        register("Pageable", "org.springframework.data.domain.Pageable");
        register("Sort", "org.springframework.data.domain.Sort");
        register("PageRequest", "org.springframework.data.domain.PageRequest");

        // Jackson
        register("JsonIgnore", "com.fasterxml.jackson.annotation.JsonIgnore");
        register("JsonProperty", "com.fasterxml.jackson.annotation.JsonProperty");
        register("JsonIgnoreProperties", "com.fasterxml.jackson.annotation.JsonIgnoreProperties");
        register("JsonFormat", "com.fasterxml.jackson.annotation.JsonFormat");

        // MapStruct & Logging
        register("Mapper", "org.mapstruct.Mapper");
        register("Mapping", "org.mapstruct.Mapping");
        register("MappingTarget", "org.mapstruct.MappingTarget");
        register("Logger", "org.slf4j.Logger");
        register("LoggerFactory", "org.slf4j.LoggerFactory");
    }

    public static void register(String className, String pkg) {
        imports.put(className.toLowerCase(), pkg);
    }

    public static String get(String key) {
        return imports.get(key.toLowerCase());
    }

    public static boolean contains(String key) {
        return imports.containsKey(key.toLowerCase());
    }
}