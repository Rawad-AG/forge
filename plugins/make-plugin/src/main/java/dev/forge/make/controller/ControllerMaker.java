package dev.forge.make.controller;

import java.util.Map;

import dev.forge.make.generators.Maker;

public final class ControllerMaker extends Maker {

    public void make(String pkg, String name, String template, boolean override) {
        super.make(pkg, name, template, override);
    }

    @Override
    protected String packageKey() {
        return "make.packages.controller";
    }

    @Override
    protected String suffixKey() {
        return "make.suffix.controller";
    }

    @Override
    protected String templateKey() {
        return "make.templates.controller";
    }

    @Override
    protected String defaultPackage() {
        return "controllers";
    }

    @Override
    protected String defaultSuffix() {
        return "Controller";
    }

    @Override
    protected String defaultTemplate() {
        return "templates/controller.mustache";
    }

    @Override
    protected Map<String, Object> extraContext(String pkg) {
        String servicePkg = config.getString("make.packages.service", "services");
        return Map.of(
                "servicePackage", pkg + "." + servicePkg);
    }
}
