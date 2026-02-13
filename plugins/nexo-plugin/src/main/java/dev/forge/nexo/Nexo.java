package dev.forge.nexo;

import dev.forge.engine.core.ForgeEngine;
import dev.forge.engine.core.ForgePlugin;
import picocli.CommandLine;

public class Nexo implements ForgePlugin {

    @Override
    public String getName() {
        return "nexo";
    }

    @Override
    public String getVersion() {
        return "0.0.1-SNAPSHOT";
    }

    @Override
    public int execute(String[] args) {
        try {
            return new CommandLine(new NexoCommand())
                    .setExecutionExceptionHandler((ex, commandLine, parseResult) -> {
                        throw ex;
                    })
                    .execute(args);

        } catch (Exception e) {
            ForgeEngine.context().console().fatal(e.getMessage());
            return 1;
        }
    }
}
