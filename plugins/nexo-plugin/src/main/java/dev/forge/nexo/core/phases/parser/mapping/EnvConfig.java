package dev.forge.nexo.core.phases.parser.mapping;

public record EnvConfig(
                String basePackage,
                Boolean useLombok,
                Boolean generateMappers,
                String dtoSuffix,
                Integer javaVersion) {
}