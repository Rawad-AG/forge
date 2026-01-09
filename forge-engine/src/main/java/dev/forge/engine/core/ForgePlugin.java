package dev.forge.engine.core;

public interface ForgePlugin {
    int execute(String[] args);

    String getName();

    String getVersion();
}
