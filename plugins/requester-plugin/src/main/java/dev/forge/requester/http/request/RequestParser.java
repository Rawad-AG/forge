package dev.forge.requester.http.request;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.forge.requester.http.HttpMethod;

public final class RequestParser {

    private RequestParser() {
    }

    public static Request parse(List<String> positionals, List<String> rawHeaders, String rawBody,
            Map<String, String> environment) {
        HttpMethod method = HttpMethod.GET;
        String url;

        positionals = positionals.stream().map(a -> parseArg(a, environment)).toList();
        rawHeaders = rawHeaders.stream().map(a -> parseArg(a, environment)).toList();
        rawBody = parseArg(rawBody, environment);

        if (positionals.size() == 1) {
            url = positionals.get(0);
        } else {
            try {
                method = HttpMethod.valueOf(positionals.get(0).toUpperCase());
                url = positionals.get(1);
            } catch (Exception e) {
                throw new IllegalArgumentException("Unknown HTTP method: " + positionals.get(0));
            }
        }

        Map<String, List<String>> headers = parseHeaders(rawHeaders);
        Body body = parseBody(rawBody);

        return new Request(method, url, headers, body);
    }

    private static Map<String, List<String>> parseHeaders(List<String> rawHeaders) {
        Map<String, List<String>> headers = new LinkedHashMap<>();

        for (String header : rawHeaders) {
            int idx = header.indexOf(':');
            if (idx <= 0)
                throw new IllegalArgumentException("Invalid header format: " + header);

            String name = header.substring(0, idx).trim();
            String value = header.substring(idx + 1).trim();

            headers.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        }

        return headers;
    }

    private static Body parseBody(String rawBody) {
        if (rawBody == null)
            return null;

        Path path = Path.of(rawBody);
        if (path.toFile().exists() && path.toFile().isFile())
            try {
                return new Body.File(Files.readString(path));
            } catch (IOException e) {
                throw new RuntimeException("unable to read file: " + e.getMessage());
            }

        return new Body.Inline(rawBody);
    }

    // ╔═════════════════════════════════════════════════════════════╗
    // ║ Internals
    // ╚═════════════════════════════════════════════════════════════╝
    private static String parseArg(String a, Map<String, String> env) {
        if (a == null)
            return null;

        for (var entry : env.entrySet()) {
            a = a.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return a;
    }
}
