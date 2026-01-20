package dev.forge.requester.http.response;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.http.HttpStatus;
import dev.forge.requester.http.curl.CurlResult;

public final class ResponsePrinter {
    private static final Console console = ForgeEngine.context().console();

    private ResponsePrinter() {
    }

    public static void print(CurlResult result, boolean realtime) {
        if (result.exitCode() == 0) {
            console.success("Request completed successfully (exit code 0)");
        } else {
            console.warn("Request failed (exit code " + result.exitCode() + ")");
        }

        console.info("Duration: " + result.durationMillis() + " ms");

        HttpStatus status = HttpStatus.fromCode(result.code());
        String statusText;
        Ansi.Color color;

        if (status == null) {
            statusText = "Unknown status code: " + result.code();
            color = Color.DEFAULT;
        } else {
            statusText = status.name() + " (" + status.code() + ")";
            if (status.isSuccess())
                color = Color.GREEN;
            else if (status.isClientError())
                color = Color.RED;
            else if (status.isServerError())
                color = Color.YELLOW;
            else
                color = Color.DEFAULT;
        }

        if (!result.stderr().isBlank() && !realtime) {
            console.line();
            console.warn("Warnings / errors:");
            console.println(result.stderr());
        }

        if (!result.stdout().isBlank()) {
            console.line();
            console.info("Response:");
            console.println(Ansi.ansi().fg(color).a("Status: " + statusText).reset().toString() + "\n");
            console.println(result.stdout());
        }
    }

}
