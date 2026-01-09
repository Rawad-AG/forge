package dev.forge.plugins_manager.install.metadata;

import java.util.Map;

import lombok.Data;

@Data
public class ForgeMetadata {
    private Map<String, Plugin> plugins;
    private Map<String, Library> lib;

    public Plugin getPlugin(String name) {
        return plugins.get(name);
    }

    public Library getLibrary(String name) {
        return lib.get(name);
    }

}
