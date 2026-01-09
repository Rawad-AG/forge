package dev.forge.make;

import dev.forge.engine.core.ForgePlugin;
import picocli.CommandLine;

public class Make implements ForgePlugin {

    @Override
    public String getName() {
        return "make";
    }

    @Override
    public String getVersion() {
        return "0.0.1-SNAPSHOT";
    }

    @Override
    public int execute(String[] args) {
        return new CommandLine(new MakeCommand()).execute(args);
    }
}
