package dev.forge.plugins_manager.install;

import java.nio.file.Path;
import java.util.List;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.plugins_manager.install.metadata.ForgeMetadata;
import dev.forge.plugins_manager.install.metadata.Library;
import dev.forge.plugins_manager.install.metadata.MetaDataLoader;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "pull", description = "pull jars")
public class PullCommand implements Runnable {

    @Parameters(arity = "1..*", paramLabel = "PLUGIN", description = "Jars to pull")
    private List<String> libs;

    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        ForgeMetadata meta = new MetaDataLoader().load();
        for (String p : libs) {
            String[] parts = p.split(":");
            String libraryName = parts[0];
            String version = parts.length == 2 ? parts[1] : "latest";

            Library lib = meta.getLibrary(libraryName);
            if (lib == null) {
                console.error("Library not found: " + libraryName + ":" + version);
                continue;
            }

            String downloadUrl = lib.resolve(version);
            if (downloadUrl == null || downloadUrl.isBlank()) {
                console.error("Version of library not found: " + libraryName + "-" + version);
                continue;
            }

            try {
                Path target = Path.of(".").resolve(p + ".jar");
                new Downloader().download(downloadUrl, target, p);
                console.success("Installed: " + p);
            } catch (Exception e) {
                console.error("unable to download library", e);
            }
        }

    }

}
