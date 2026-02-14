package dev.forge.nexo.core;

import java.nio.file.Path;
import java.util.List;

import dev.forge.nexo.core.phases.modeler.Modeler;
import dev.forge.nexo.core.phases.normalizer.Normalizer;
import dev.forge.nexo.core.phases.outputer.Outputer;
import dev.forge.nexo.core.phases.parser.ParsePhase;
import dev.forge.nexo.core.phases.templater.Templater;
import dev.forge.nexo.core.phases.validator.Validator;

public class NexoEngine {
    private final Path path;
    private final List<Runnable> phases = List.of(
            new ParsePhase(),
            new Normalizer(),
            new Validator(),
            new Modeler(),
            new Templater(),
            new Outputer());

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
