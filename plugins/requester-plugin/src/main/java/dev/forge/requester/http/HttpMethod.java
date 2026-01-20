package dev.forge.requester.http;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

public enum HttpMethod {
    GET(Color.GREEN),
    POST(Color.YELLOW),
    PUT(Color.CYAN),
    PATCH(Color.MAGENTA),
    DELETE(Color.RED),
    HEAD(Color.BLUE),
    OPTIONS(Color.DEFAULT);

    private final Color color;

    HttpMethod(Color color) {
        this.color = color;
    }

    public String colorize() {
        return Ansi.ansi()
                .fg(color)
                .a(toString())
                .reset()
                .toString();
    }
}
