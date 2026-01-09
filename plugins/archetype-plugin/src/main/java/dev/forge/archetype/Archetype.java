package dev.forge.archetype;

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
        return new CommandLine(new ArchetypeCommand()).execute(args);
    }
}
