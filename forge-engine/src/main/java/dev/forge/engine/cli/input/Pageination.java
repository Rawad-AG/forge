package dev.forge.engine.cli.input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = true)
public class Pageination<T> {
    private int size = 10;
    private final List<T> options;
    private final Console console = ForgeEngine.context().console();
    private final Prompter prompter = ForgeEngine.context().prompter();
    private T defaultOption;

    public Pageination(Collection<T> options) {
        if (options == null || options.isEmpty())
            throw new RuntimeException("empty options");

        this.options = new ArrayList<>(options);
    }

    public T run() {
        int last = 0;

        while (true) {
            int from = Math.max(0, last);
            int to = Math.min(options.size(), last + size);
            printChunk(from, to);

            String input = null;
            if (defaultOption != null) {
                input = prompter.askOrDefault("choose (use n/p to navigate)", defaultOption.toString());
                if (defaultOption.toString().equals(input))
                    return defaultOption;
            } else
                input = prompter.askRequired("choose (use n/p to navigate)");

            if ("n".equalsIgnoreCase(input)) {
                if (last + size < options.size())
                    last += size;
                continue;
            }
            if ("p".equalsIgnoreCase(input)) {
                if (last - size >= 0)
                    last -= size;
                continue;
            }

            try {
                int choice = Integer.parseInt(input) - 1;
                if (choice >= from && choice < to)
                    return options.get(choice);

            } catch (NumberFormatException e) {
                console.error("invalid input");
                continue;
            }
        }

    }

    private void printChunk(int from, int to) {
        console.clear();
        for (int i = from; i < to; i++)
            console.println(i + 1 + ". " + options.get(i));

        if (options.size() > size) {
            console.line();
            console.info("(" + from + ", " + to + ")" + " / " + options.size());
        }
    }

}
