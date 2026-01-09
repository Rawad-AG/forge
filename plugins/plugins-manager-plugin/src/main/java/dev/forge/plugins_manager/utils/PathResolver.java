package dev.forge.plugins_manager.utils;

import java.nio.file.Path;

import dev.forge.engine.core.ForgeEngine;

public class PathResolver {

    public static Path ForgePluginsPath() {
        return Path.of(
                ForgeEngine.context().config().getString("forge.directory").replaceAll("~",
                        System.getProperty("user.home")),
                "plugins");
    }
}
