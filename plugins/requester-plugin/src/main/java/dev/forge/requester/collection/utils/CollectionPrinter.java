package dev.forge.requester.collection.utils;

import java.util.List;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.collection.json.MetaData;
import dev.forge.requester.collection.json.collection.Collection;
import dev.forge.requester.collection.json.collection.CollectionItem;
import dev.forge.requester.http.request.Body;

public final class CollectionPrinter {

    private static final Console console = ForgeEngine.context().console();

    private static final int INDENT_STEP = 4;
    private static final int NAME_COLUMN_WIDTH = 30;

    public static void printCollection(Collection c) {
        printMetaData(c.meta(), 0, "🗃");
        for (var item : c.items()) {
            printItem(item, 0);
        }
    }

    public static void printCollections(List<Collection> collections) {
        for (Collection c : collections)
            printMetaData(c.meta(), 0, "🗃");
    }

    public static void printRequest(CollectionItem.Request req) {
        printItem(req, 0);
    }

    public static void printFolder(CollectionItem.Folder folder) {
        printItem(folder, 0);
    }

    // ╔═════════════════════════════════════════════════════════════╗
    // ║ Internals
    // ╚═════════════════════════════════════════════════════════════╝

    private static String indent(int depth) {
        return " ".repeat(depth * INDENT_STEP);
    }

    private static String padRight(String text, int width) {
        if (text.length() >= width)
            return text;
        return text + " ".repeat(width - text.length());
    }

    private static void printMetaData(MetaData meta, int depth, String defaultIcon) {
        String icon = meta.icon() != null ? meta.icon() : defaultIcon;

        String header = Ansi.ansi()
                .fg(meta.color() != null ? meta.color() : Color.DEFAULT)
                .a(icon + "  " + meta.name())
                .reset()
                .toString();

        String line1 = indent(depth) + padRight(header, NAME_COLUMN_WIDTH) + "  (" + meta.id() + ")";
        String line2 = indent(depth) + "  " + meta.description();

        console.println(line1);
        console.println(line2);
    }

    private static void printItem(CollectionItem item, int depth) {
        console.line();

        if (item instanceof CollectionItem.Folder folder) {
            printMetaData(folder.meta(), depth, "🖿");
            folder.items().forEach(child -> printItem(child, depth + 1));
        } else if (item instanceof CollectionItem.Request req) {
            printMetaData(req.meta(), depth, "🌐");

            String method = req.method().colorize();
            String url = req.url();

            String line = indent(depth) + padRight(method, 10) + "  " + url;
            console.println(line);

            if (req.body() instanceof Body.File fileBody) {
                console.println(indent(depth) + "Body in file: " + fileBody.path());
            }

            if (req.body() instanceof Body.Inline inlineBody) {
                console.println(indent(depth) + "Body:");
                console.println(indent(depth) + inlineBody.content());
            }
        }
    }
}
