package dev.forge.requester.collection.json.collection;

public record RequestOptions(
        boolean enabled,
        Integer timeoutMs,
        Integer retries) {
    public static RequestOptions defaults() {
        return new RequestOptions(true, null, null);
    }
}
