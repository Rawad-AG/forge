package dev.forge.make.generators;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.engine.utils.ProjectUtils;

public abstract class Maker {

    protected static final ForgeConfigLoader config = ForgeEngine.context().config();

    protected void make(
            String pkg,
            String name,
            String template,
            boolean override) {

        Path base = Path.of("src", "main", "java");
        pkg = resolvePackage(pkg);

        for (String part : pkg.split("\\."))
            base = base.resolve(part);

        String subPackage = config.getString(packageKey(), defaultPackage());
        base = base.resolve(subPackage);

        String suffix = config.getString(suffixKey(), defaultSuffix()) + ".java";
        base = base.resolve(name + suffix);

        template = resolveTemplate(template);

        Map<String, Object> context = new HashMap<>(baseContext(pkg, name));
        context.putAll(extraContext(pkg));

        new FileMaker()
                .override(override)
                .generate(base, template, context);
    }

    private String resolvePackage(String pkg) {
        pkg = pkg != null && !pkg.isBlank() ? pkg : ProjectUtils.getRootPackage();
        if (pkg == null || pkg.isBlank())
            throw new RuntimeException("unable to auto detect the package. please pass the --pkg option");
        return pkg;
    }

    private String resolveTemplate(String template) {
        return template != null && !template.isBlank()
                ? template
                : config.getString(templateKey(), defaultTemplate());
    }

    protected Map<String, Object> baseContext(String pkg, String name) {
        return Map.of(
                "package", pkg + "." + config.getString(packageKey(), defaultPackage()),
                "rootPackage", pkg,
                "className", name,
                "classNameLower", name.toLowerCase());
    }

    protected abstract String packageKey();

    protected abstract String suffixKey();

    protected abstract String templateKey();

    protected abstract String defaultPackage();

    protected abstract String defaultSuffix();

    protected abstract String defaultTemplate();

    protected abstract Map<String, Object> extraContext(String pkg);
}
