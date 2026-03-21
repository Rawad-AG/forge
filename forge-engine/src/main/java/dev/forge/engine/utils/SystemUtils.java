package dev.forge.engine.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;

public class SystemUtils {
    private static final Console console = ForgeEngine.context().console();

    public static void openBrowser(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                console.success("Opened browser via AWT.");
            } else {
                String os = System.getProperty("os.name").toLowerCase();
                Runtime rt = Runtime.getRuntime();

                if (os.contains("win")) {
                    rt.exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", url });
                } else if (os.contains("mac")) {
                    rt.exec(new String[] { "open", url });
                } else if (os.contains("nix") || os.contains("nux")) {
                    rt.exec(new String[] { "xdg-open", url });
                } else {
                    throw new IOException("Unknown OS, cannot launch browser.");
                }
                console.success("Opened browser via OS fallback.");
            }
        } catch (Exception e) {
            console.warn("Could not open browser: " + e.getMessage());
            console.info("Open manually: " + url);
        }
    }
}