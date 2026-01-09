package dev.forge.plugins_manager.install.metadata;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;

public class MetaDataLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Console console = ForgeEngine.context().console();
    private final ForgeConfigLoader config = ForgeEngine.context().config();

    public ForgeMetadata load() {
        try {
            String url = config.getString("pluginManager.repo.metadata");
            console.startLoader("fetching the repo...");
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            console.stopLoader();

            if (response.statusCode() != 200)
                console.fatal("Failed to fetch metadata. HTTP code: " + response.statusCode());

            console.success("repo fetched successfully");
            return MAPPER.readValue(response.body(), ForgeMetadata.class);
        } catch (IOException | InterruptedException e) {
            console.stopLoader();
            console.fatal("Error loading Forge metadata", e);
        }

        return null;
    }

}
