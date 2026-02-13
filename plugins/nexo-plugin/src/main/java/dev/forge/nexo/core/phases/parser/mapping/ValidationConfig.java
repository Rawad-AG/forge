package dev.forge.nexo.core.phases.parser.mapping;

public record ValidationConfig(
                Boolean notNull,
                Boolean notEmpty,
                SizeConfig size,
                String pattern,
                Boolean email,
                Integer min,
                Integer max) {
}