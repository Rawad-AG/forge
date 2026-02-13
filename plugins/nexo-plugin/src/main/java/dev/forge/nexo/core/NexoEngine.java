package dev.forge.nexo.core;

import java.nio.file.Path;
import java.util.List;

import dev.forge.nexo.core.phases.normalizer.Normalizer;
import dev.forge.nexo.core.phases.parser.ParsePhase;

public class NexoEngine {
    private final Path path;
    private final List<Runnable> phases = List.of(
            new ParsePhase(),
            new Normalizer());

    public NexoEngine(String path) {
        if (!path.endsWith(".json"))
            throw new IllegalArgumentException("provide a valid path to a Json file, or default to ./nexo.json");

        this.path = Path.of(path);

        if (!this.path.toFile().exists())
            throw new IllegalArgumentException("file does not exists at path: " + path);

        NexoContext.put(RegistryKey.FILE, new DataBox(this.path.toFile()));
    }

    public void runPipeline() {
        phases.forEach(p -> p.run());
    }
}
