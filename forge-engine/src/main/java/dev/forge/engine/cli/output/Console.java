package dev.forge.engine.cli.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

import dev.forge.engine.cli.context.TerminalContext;
import dev.forge.engine.cli.styles.Styler;
import dev.forge.engine.cli.styles.Styles;
import lombok.NonNull;

public final class Console {

    private Thread loaderThread;
    private final AtomicBoolean loaderRunning = new AtomicBoolean(false);

    private final TerminalContext ctx;
    private final boolean useAnsi;

    public Console(@NonNull TerminalContext ctx) {
        this.ctx = ctx;
        this.useAnsi = ctx.isInteractive() && ctx.isAnsiSupported();
    }

    public void info(String msg) {
        println(style(Styles::info, "> " + msg));
    }

    public void success(String msg) {
        println(style(Styles::success, "> " + msg));
    }

    public void warn(String msg) {
        println(style(Styles::warn, "⚠ " + msg));
    }

    public void error(String msg, Exception... exs) {
        println(style(Styles::error, "🛇 " + msg));
        if (exs != null)
            for (Exception e : exs) {
                println(style(Styles::error, "> " + e.getMessage()));
                logToFile(msg, e);
            }

    }

    public void fatal(String msg, Exception... exs) {
        error(msg, exs);
        System.exit(1);
    }

    private void logToFile(String msg, Exception e) {
        try {
            File forgeDir = new File(System.getProperty("user.home"), ".forge");
            if (!forgeDir.exists())
                forgeDir.mkdirs();

            File logFile = new File(forgeDir, "log.txt");

            try (PrintWriter pw = new PrintWriter(new FileWriter(logFile, true))) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                pw.println("[" + timestamp + "] " + msg);

                e.printStackTrace(pw);

                pw.println();
            }

        } catch (IOException ignored) {
        }
    }

    public void println(String msg) {
        ctx.out().println(msg);
        ctx.out().flush();
    }

    public void print(String msg) {
        ctx.out().print(msg);
        ctx.out().flush();
    }

    public void line() {
        println("");
    }

    public void progressbar(double current, double total, String... prefixes) {
        int width = Math.min(50, width() - 10);
        double fraction = current / total;
        int filled = (int) (fraction * width);
        int empty = width - filled;

        String bar = "▰".repeat(filled) + "▱".repeat(empty) + "    ";
        String percent = String.format("%3d%%", (int) (fraction * 100));

        print("\r" + bar + percent + "    " + String.join(" ", prefixes));

        if (current >= total)
            line();
    }

    public void startLoader(String message) {
        if (loaderRunning.get())
            return;
        loaderRunning.set(true);

        loaderThread = new Thread(() -> {
            char[] spinner = { '⣸', '⣴', '⣦', '⣇', '⡏', '⠟', '⠻', '⢹' };
            int i = 0;
            while (loaderRunning.get()) {
                ctx.out().print("\r" + message + "  " + spinner[i % spinner.length]);
                ctx.out().flush();
                i++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            ctx.out().print("\r");
        });

        loaderThread.start();
    }

    public void stopLoader() {
        if (!loaderRunning.get())
            return;
        loaderRunning.set(false);
        try {
            loaderThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        line();
    }

    public int width() {
        return ctx.width();
    }

    public boolean isInteractive() {
        return ctx.isInteractive();
    }

    public String style(Styler styler, String msg) {
        return useAnsi ? styler.apply(msg) : msg;
    }

}
