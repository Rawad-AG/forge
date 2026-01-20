package dev.forge.archetype;

import dev.forge.engine.core.ForgeEngine;
import dev.forge.engine.core.ForgePlugin;
import picocli.CommandLine;

public class Archetype implements ForgePlugin {

    @Override
    public String getName() {
        return "archetype";
    }

    @Override
    public String getVersion() {
        return "0.0.1-SNAPSHOT";
    }

    @Override
    public int execute(String[] args) {
        try {
            int exitCode = new CommandLine(new ArchetypeCommand()).execute(args);
            return exitCode;
        } catch (Exception e) {
            ForgeEngine.context().console().fatal(e.getMessage());
            return 1;
        }
    }
}
