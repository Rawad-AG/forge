package dev.forge.engine.cli.context;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;

import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true, chain = true)
public final class TerminalContext implements AutoCloseable {

    private static final AtomicBoolean ANSI_INSTALLED = new AtomicBoolean(false);

    private final Terminal terminal;
    private final LineReader lineReader;
    private final PrintWriter out;
    private final boolean ownsAnsi;

    private TerminalContext(Terminal terminal, LineReader lineReader, boolean ownsAnsi) {
        this.terminal = terminal;
        this.lineReader = lineReader;
        this.out = terminal.writer();
        this.ownsAnsi = ownsAnsi;
    }

    public static TerminalContext create() {
        String os = System.getProperty("os.name");

        if (os.startsWith("Windows"))
            return windowsCreation();

        if ("Linux".equals(os) || "Mac OS X".equals(os))
            return unixCreation();

        throw new RuntimeException("unsupported OS " + os);
    }

    public boolean isInteractive() {
        return terminal.getAttributes() != null;
    }

    public boolean isAnsiSupported() {
        if (!isInteractive())
            return false;

        String term = System.getenv("TERM");
        if (term == null || term.trim().isEmpty() || term.equalsIgnoreCase("dumb"))
            return false;

        return true;
    }

    public int width() {
        return terminal.getWidth();
    }

    @Override
    public void close() {
        try {
            terminal.close();

        } catch (IOException ignored) {

        } finally {
            if (ownsAnsi && ANSI_INSTALLED.compareAndSet(true, false))
                AnsiConsole.systemUninstall();
        }
    }

    // *============================================================
    // *= Internals
    // *============================================================
    private static TerminalContext windowsCreation() {
        try {
            boolean installed = false;
            if (ANSI_INSTALLED.compareAndSet(false, true)) {
                AnsiConsole.systemInstall();
                installed = true;
            }

            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .encoding(Charset.forName("UTF-8"))
                    .build();

            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            return new TerminalContext(terminal, reader, installed);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize terminal on Windows", e);
        }
    }

    private static TerminalContext unixCreation() {
        try {
            boolean installed = false;

            if (ANSI_INSTALLED.compareAndSet(false, true)) {
                AnsiConsole.systemInstall();
                installed = true;
            }

            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .encoding(Charset.forName("UTF-8"))
                    .jna(true)
                    .build();

            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            return new TerminalContext(terminal, reader, installed);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize terminal", e);
        }
    }
}
