package dev.forge.codeforces.search.cache;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.forge.codeforces.search.models.ProblemsetProblemsResponse;
import dev.forge.engine.core.ForgeEngine;

public class ProblemCache {
    private static final String CACHE_DIR = System.getProperty("user.home") + "/.forge/codeforces";
    private static final String CACHE_FILE = CACHE_DIR + "/problems.json";
    private static final long CACHE_DURATION_MS = ForgeEngine.context().config()
            .getLong("flush-cache-after", 24 * 60 * 60 * 1000L);

    private final ObjectMapper objectMapper;

    public ProblemCache() {
        this.objectMapper = new ObjectMapper();
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }


    public void InvalidateCache() {
        File cacheFile = new File(CACHE_FILE);
        if(cacheFile.exists())
            cacheFile.delete();
    }

    public ProblemsetProblemsResponse getCachedProblems() {
        File cacheFile = new File(CACHE_FILE);
        if (!cacheFile.exists()) {
            return null;
        }

        long now = System.currentTimeMillis();
        long lastModified = cacheFile.lastModified();

        if (now - lastModified > CACHE_DURATION_MS) {
            cacheFile.delete();
            return null;
        }

        try (FileReader reader = new FileReader(cacheFile)) {
            return objectMapper.readValue(reader, ProblemsetProblemsResponse.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void cacheProblems(ProblemsetProblemsResponse response) {
        File cacheFile = new File(CACHE_FILE);
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(cacheFile);
                java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(fos)) {

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(bos, response);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProblemsetProblemsResponse fetchProblemsFromApi() throws IOException {
        String url = "https://codeforces.com/api/problemset.problems";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<java.io.InputStream> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200)
                throw new IOException("API returned status code: " + response.statusCode());

            return objectMapper.readValue(response.body(), ProblemsetProblemsResponse.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
}
