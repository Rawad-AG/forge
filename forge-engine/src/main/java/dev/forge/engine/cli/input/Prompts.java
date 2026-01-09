package dev.forge.engine.cli.input;

import java.util.Locale;

import org.jline.reader.LineReader;

import dev.forge.engine.cli.context.TerminalContext;
import dev.forge.engine.core.ForgeEngine;

public final class Prompts {

    private final TerminalContext ctx;

    public Prompts(TerminalContext ctx) {
        this.ctx = ctx;
    }

    private String readLine(String prompt) {
        if (!ctx.isInteractive())
            return null;

        LineReader reader = ctx.lineReader();
        try {
            return reader.readLine(prompt);
        } catch (Exception e) {
            return null;
        }
    }

    public String askRequired(String question) {
        if (!ctx.isInteractive())
            throw new IllegalStateException("Required input in non-interactive mode");

        while (true) {
            String input = readLine(question + ": ");
            if (input != null && !input.isBlank())
                return input.trim();

            ForgeEngine.context().console().println("Input is required.");
        }
    }

    public String askOrDefault(String question, String defaultValue) {
        if (!ctx.isInteractive())
            return defaultValue;

        String prompt = question + " [" + defaultValue + "]: ";

        while (true) {
            String input = readLine(prompt);
            if (input == null || input.isBlank())
                return defaultValue;

            return input.trim();
        }
    }

    public String askOrNull(String question) {
        String input = readLine(question + ": ");
        return (input == null || input.isBlank()) ? null : input.trim();
    }

    public boolean confirmRequired(String question) {
        if (!ctx.isInteractive())
            throw new IllegalStateException("Required confirmation in non-interactive mode");

        while (true) {
            String input = readLine(question + " [y/n]: ");
            if (input == null || input.isBlank()) {
                ForgeEngine.context().console().println("Please enter y or n.");
                continue;
            }

            char c = input.trim().toLowerCase(Locale.ROOT).charAt(0);
            if (c == 'y')
                return true;
            if (c == 'n')
                return false;

            ForgeEngine.context().console().println("Please enter y or n.");
        }
    }

    public boolean confirmOrDefault(String question, boolean defaultValue) {
        if (!ctx.isInteractive())
            return defaultValue;

        String suffix = defaultValue ? " [Y/n]: " : " [y/N]: ";

        while (true) {
            String input = readLine(question + suffix);
            if (input == null || input.isBlank())
                return defaultValue;

            char c = input.trim().toLowerCase(Locale.ROOT).charAt(0);
            if (c == 'y')
                return true;
            if (c == 'n')
                return false;

            ForgeEngine.context().console().println("Please enter y or n.");
        }
    }

    public int askIntRequired(String question) {
        if (!ctx.isInteractive())
            throw new IllegalStateException("Required integer input in non-interactive mode");

        while (true) {
            String input = readLine(question + ": ");
            try {
                return Integer.parseInt(input.trim());
            } catch (Exception e) {
                ForgeEngine.context().console().println("Invalid integer. Try again.");
            }
        }
    }

    public int askIntOrDefault(String question, int defaultValue) {
        if (!ctx.isInteractive())
            return defaultValue;

        while (true) {
            String input = readLine(question + " [" + defaultValue + "]: ");
            if (input == null || input.isBlank())
                return defaultValue;

            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                ForgeEngine.context().console().println("Invalid integer. Try again.");
            }
        }
    }

    public double askDoubleRequired(String question) {
        if (!ctx.isInteractive())
            throw new IllegalStateException("Required number input in non-interactive mode");

        while (true) {
            String input = readLine(question + ": ");
            try {
                return Double.parseDouble(input.trim());
            } catch (Exception e) {
                ForgeEngine.context().console().println("Invalid number. Try again.");
            }
        }
    }

    public double askDoubleOrDefault(String question, double defaultValue) {
        if (!ctx.isInteractive())
            return defaultValue;

        while (true) {
            String input = readLine(question + " [" + defaultValue + "]: ");
            if (input == null || input.isBlank())
                return defaultValue;

            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                ForgeEngine.context().console().println("Invalid number. Try again.");
            }
        }
    }

    public String chooseRequired(String question, String... options) {
        if (!ctx.isInteractive())
            throw new IllegalStateException("Required choice in non-interactive mode");

        String opts = String.join(" | ", options);

        while (true) {
            String input = readLine(question + " [" + opts + "]: ");
            for (String option : options)
                if (option.equals(input))
                    return option;

            ForgeEngine.context().console().println("Invalid choice. Expected one of: " + opts);
        }
    }

    public String chooseOrDefault(String question, String defaultValue, String... options) {
        if (!ctx.isInteractive())
            return defaultValue;

        String opts = String.join(" | ", options);

        while (true) {
            String input = readLine(question + " [" + opts + "] [" + defaultValue + "]: ");
            if (input == null || input.isBlank())
                return defaultValue;

            for (String option : options)
                if (option.equals(input))
                    return option;

            ForgeEngine.context().console().println("Invalid choice. Expected one of: " + opts);
        }
    }

}
