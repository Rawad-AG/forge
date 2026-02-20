package dev.forge.nexo.core.phases.outputer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.modeler.models.JavaFileModel;

public class Outputer implements Runnable {

    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        Map<JavaFileModel, String> outputs = NexoContext.get(RegistryKey.Template_Outputs);
        List<Path> created = new ArrayList<>();

        console.info("=== Generated Files ===");

        for (var entry : outputs.entrySet()) {
            try {
                JavaFileModel model = entry.getKey();
                String content = entry.getValue();

                Path dir = Path.of("src/main/java");
                for (String p : model.getPkg().split("\\."))
                    dir = dir.resolve(p);

                Files.createDirectories(dir);

                Path file = dir.resolve(model.getName() + ".java");

                Files.writeString(file, content, StandardOpenOption.CREATE);

                created.add(file);
                console.success(file + " created successfully");

            } catch (IOException e) {

                created.forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });

                throw new RuntimeException("Generation failed: " + e.getMessage(), e);
            }
        }
    }
}