package dev.forge.engine.cli.styles;

@FunctionalInterface
public interface Styler {
    String apply(String s);
}