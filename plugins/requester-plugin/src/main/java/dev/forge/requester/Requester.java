package dev.forge.requester;

import dev.forge.engine.core.ForgeEngine;
import dev.forge.engine.core.ForgePlugin;
import picocli.CommandLine;

public class Requester implements ForgePlugin {

    @Override
    public String getName() {
        return "requester";
    }

    @Override
    public String getVersion() {
        return "0.0.1-SNAPSHOT";
    }

    @Override
    public int execute(String[] args) {
        try {
            int exitCode = new CommandLine(new RequesterCommand()).execute(args);
            return exitCode;
        } catch (Exception e) {
            ForgeEngine.context().console().fatal(e.getMessage());
            return 1;
        }
    }
}
