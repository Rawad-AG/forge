package dev.forge.make.service;

import java.nio.file.Path;
import java.util.Map;

import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.make.generators.FileMaker;
import dev.forge.make.utils.ProjectUtils;

public class ServiceMaker {
    private static final ForgeConfigLoader config = ForgeEngine.context().config();

    public static void make(String pkg, String name, String template, boolean override) {

        Path base = Path.of("src", "main", "java");
        pkg = pkg != null && !pkg.isBlank() ? pkg : ProjectUtils.getRootPackage();

        if (pkg == null || pkg.isBlank())
            throw new RuntimeException("unable to auto detect the package. please pass the --pkg option");

        for (String part : pkg.split("\\."))
            base = base.resolve(part);

        base = base.resolve(config.getString("make.packages.service", "services"));

        String suffix = config.getBoolean("make.addSuffix", true) ? "Service.java" : ".java";

        base = base.resolve(name + suffix);
        template = template != null && !template.isBlank() ? template
                : config.getString("make.templates.service", "templates/service.mustache");
        String repoPkg = config.getString("make.packages.repository", "repositories");

        new FileMaker().override(override).generate(base, template, Map.of(
                "package", pkg + "." + config.getString("make.packages.service", "services"),
                "className", name,
                "classNameLower", name.toLowerCase(),
                "repoPackage", pkg + "." + repoPkg));
    }

}
