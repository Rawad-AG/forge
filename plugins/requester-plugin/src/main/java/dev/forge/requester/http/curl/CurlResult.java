package dev.forge.requester.http.curl;

public record CurlResult(
        int exitCode,
        String stdout,
        String stderr,
        int code,
        long durationMillis) {
}
