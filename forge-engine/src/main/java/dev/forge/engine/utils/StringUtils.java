package dev.forge.engine.utils;

public class StringUtils {
    public static String capitalize(String s) {
        return (s.charAt(0) + "").toUpperCase() + s.substring(1);
    }
}
