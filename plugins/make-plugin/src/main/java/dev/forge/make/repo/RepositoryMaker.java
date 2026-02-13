package dev.forge.make.repo;

import java.util.Map;

import dev.forge.make.generators.Maker;

public final class RepositoryMaker extends Maker {

    public void make(String pkg, String name, String template, boolean override) {
        super.make(pkg, name, template, override);
    }

    protected String packageKey() {
        return "make.packages.repository";
    }

    protected String suffixKey() {
        return "make.suffix.repository";
    }

    protected String templateKey() {
        return "make.templates.repository";
    }

    protected String defaultPackage() {
        return "repositories";
    }

    protected String defaultSuffix() {
        return "Repository";
    }

    protected String defaultTemplate() {
        return "templates/repository.mustache";
    }

    protected Map<String, Object> extraContext(String pkg) {
        String entityPkg = config.getString("make.packages.entity", "entities");
        return Map.of(
                "entityPackage", pkg + "." + entityPkg);
    }
}
