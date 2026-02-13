package dev.forge.make.entity;

import java.util.Map;

import dev.forge.make.generators.Maker;

public final class EntityMaker extends Maker {

    public void make(String pkg, String name, String template, boolean override) {
        super.make(pkg, name, template, override);
    }

    @Override
    protected String packageKey() {
        return "make.packages.entity";
    }

    @Override
    protected String suffixKey() {
        return "make.suffix.entity";
    }

    @Override
    protected String templateKey() {
        return "make.templates.entity";
    }

    @Override
    protected String defaultPackage() {
        return "entities";
    }

    @Override
    protected String defaultSuffix() {
        return "Entity";
    }

    @Override
    protected String defaultTemplate() {
        return "templates/entity.mustache";
    }

    @Override
    protected Map<String, Object> extraContext(String pkg) {
        return Map.of(); // intentionally empty
    }
}
