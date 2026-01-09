package dev.forge.plugins_manager.install.metadata;

import java.util.Map;

import lombok.Data;

@Data
public class Library {
    private Map<String, String> versions;
    private String latest;

    public String getLatest() {
        return versions.get(latest);
    }

    public String resolve(String version) {
        return "latest".equals(version) ? getLatest() : versions.get(version);
    }

    public String version(String version) {
        if ("latest".equals(version))
            return latest;

        return versions.containsKey(version) ? version : "unknown";
    }
}
