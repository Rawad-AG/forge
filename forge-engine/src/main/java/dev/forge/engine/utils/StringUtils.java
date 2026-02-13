package dev.forge.engine.utils;

public class StringUtils {
    public static String capitalize(String s) {
        return (s.charAt(0) + "").toUpperCase() + s.substring(1);
    }


    public static String camelToSnake(String str) {
        if (str == null)
            return "";
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
