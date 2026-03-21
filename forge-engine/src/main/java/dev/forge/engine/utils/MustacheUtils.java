package dev.forge.engine.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import lombok.NonNull;

public class MustacheUtils {
    private static final Console console = ForgeEngine.context().console();

    public static void create(InputStream is, File file) {
        create(is, file, new HashMap<>());
    }

    public static void create(@NonNull InputStream is, @NonNull File file, Map<String, Object> ctx) {
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs())
                throw new IOException("Could not create directory: " + parent);

            try (Writer writer = new FileWriter(file)) {
                MustacheFactory mf = new DefaultMustacheFactory();
                Mustache mustache = mf.compile(new InputStreamReader(is), file.getName());

                mustache.execute(writer, ctx);
                writer.flush();

                console.success("Created " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate file", e);
        }
    }

}
