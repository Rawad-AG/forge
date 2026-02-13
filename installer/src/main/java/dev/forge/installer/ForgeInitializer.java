package dev.forge.installer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class ForgeInitializer {

    private static final String ENGINE_JAR = "forge-engine-0.0.1-SNAPSHOT.jar";
    private static final String PLUGIN_JAR =
        "plugins-manager-0.0.1-SNAPSHOT.jar";
    private static final String CONFIG_FILE = "forge.conf";

    private ForgeInitializer() {}

    public static void initialize() throws IOException {
        Path home = Path.of(System.getProperty("user.home"));

        Path forgeDir = home.resolve(".forge");
        Path pluginsDir = forgeDir.resolve("plugins");
        Path engineDir = forgeDir.resolve("engine");

        Files.createDirectories(pluginsDir);
        Files.createDirectories(engineDir);

        copyResource(ENGINE_JAR, engineDir.resolve("forge-engine.jar"));
        copyResource(PLUGIN_JAR, pluginsDir.resolve("plugins-manager.jar"));

        Path configFile = forgeDir.resolve(CONFIG_FILE);
        if (Files.notExists(configFile)) {
            Files.createFile(configFile);
        }
    }

    private static void copyResource(String resourceName, Path target)
        throws IOException {
        if (Files.exists(target)) {
            return;
        }

        try (
            InputStream in =
                ForgeInitializer.class.getClassLoader().getResourceAsStream(
                    resourceName
                )
        ) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourceName);
            }
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
