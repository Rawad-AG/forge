package dev.forge.plugins_manager.install;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;

public class Downloader {
    private final Console console = ForgeEngine.context().console();

    public void download(String url, Path target, String... prefixes) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URI(url).toURL().openConnection();
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(10_000);

        long totalBytes = conn.getContentLengthLong();
        long downloaded = 0;

        try (InputStream in = conn.getInputStream();
                OutputStream out = Files.newOutputStream(target)) {

            byte[] buffer = new byte[8192];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                downloaded += read;

                console.progressbar(downloaded, totalBytes, prefixes);
            }
        }

        console.line();
    }
}
