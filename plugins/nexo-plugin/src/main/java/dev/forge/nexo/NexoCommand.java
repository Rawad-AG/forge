package dev.forge.nexo;

import dev.forge.nexo.core.NexoEngine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "nexo", mixinStandardHelpOptions = true)
public class NexoCommand implements Runnable {
    @Parameters(index = "0", arity = "0..1", defaultValue = "nexo.json")
    private String path;

    @Override
    public void run() {
        new NexoEngine(path).runPipeline();
    }
}