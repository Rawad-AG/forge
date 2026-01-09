package dev.forge.engine.cli.styles;

import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.*;

public final class Styles {

    private Styles() {
    }

    public static String info(String s) {
        return Ansi.ansi()
                .fg(CYAN)
                .a(s)
                .reset()
                .toString();
    }

    public static String success(String s) {
        return Ansi.ansi()
                .fg(GREEN)
                .a(s)
                .reset()
                .toString();
    }

    public static String warn(String s) {
        return Ansi.ansi()
                .fg(YELLOW)
                .a(s)
                .reset()
                .toString();
    }

    public static String error(String s) {
        return Ansi.ansi()
                .fg(RED)
                .bold()
                .a(s)
                .reset()
                .toString();
    }

    public static String bold(String s) {
        return Ansi.ansi()
                .bold()
                .a(s)
                .reset()
                .toString();
    }
}
