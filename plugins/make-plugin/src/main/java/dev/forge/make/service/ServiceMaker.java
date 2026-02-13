package dev.forge.make.service;

import java.util.Map;

import dev.forge.make.generators.Maker;

public class ServiceMaker extends Maker {
    public void make(String pkg, String name, String template, boolean override) {
        super.make(pkg, name, template, override);
    }

    protected String packageKey() {
        return "make.packages.service";
    }

    protected String suffixKey() {
        return "make.suffix.service";
    }

    protected String templateKey() {
        return "make.templates.service";
    }

    protected String defaultPackage() {
        return "services";
    }

    protected String defaultSuffix() {
        return "Service";
    }

    protected String defaultTemplate() {
        return "templates/service.mustache";
    }

    protected Map<String, Object> extraContext(String pkg) {
        String repoPkg = config.getString("make.packages.repository", "repositories");
        return Map.of(
                "repoPackage", pkg + "." + repoPkg);
    }
}
