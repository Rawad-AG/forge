package dev.forge.requester.http.curl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;

public final class CurlExecutor {
    private static final Console console = ForgeEngine.context().console();

    private CurlExecutor() {
    }

    /**
     * Execute the curl command.
     *
     * @param curl     the compiled curl command
     * @param realtime if true, stdout/stderr print live to Console
     * @return CurlResult containing exit code, stdout, stderr, and duration
     */
    public static CurlResult execute(CurlCommand curl, boolean realtime) {
        Instant start = Instant.now();
        ProcessBuilder pb = new ProcessBuilder(curl.args());

        StringBuilder stdoutBuffer = new StringBuilder();
        StringBuilder stderrBuffer = new StringBuilder();

        try {
            if (!realtime) {
                console.startLoader("Sending request");
            }

            Process process = pb.start();

            Thread outThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stdoutBuffer.append(line).append("\n");
                    }
                } catch (Exception e) {
                    console.warn("Error reading stdout: " + e.getMessage());
                }
            });

            Thread errThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    int i = 0;
                    while ((line = reader.readLine()) != null) {
                        stderrBuffer.append(line).append("\n");
                        i++;
                        if (realtime) {
                            console.print(line);
                            if (i > 2)
                                console.backR();
                            else
                                console.line();
                        }
                    }
                } catch (Exception e) {
                    console.warn("Error reading stderr: " + e.getMessage());
                }
            });

            outThread.start();
            errThread.start();

            int exitCode = process.waitFor();

            outThread.join();
            errThread.join();

            if (!realtime) {
                console.stopLoader();
            } else
                console.line();

            long duration = Duration.between(start, Instant.now()).toMillis();
            String fullOutput = stdoutBuffer.toString();

            String body = fullOutput;
            int httpStatus = -1;
            if (fullOutput.contains("__HTTP_CODE__:")) {
                String[] parts = fullOutput.split("__HTTP_CODE__:");
                if (parts.length == 2) {
                    body = parts[0];
                    try {
                        httpStatus = Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException nfe) {
                        console.warn("Failed to parse HTTP status code: " + parts[1]);
                        httpStatus = -1;
                    }
                } else {
                    console.warn("Unexpected split result for HTTP status code: " + fullOutput);
                }
            } else {
                console.warn("No HTTP status code marker found in output.");
            }

            return new CurlResult(exitCode, body, stderrBuffer.toString(), httpStatus, duration);

        } catch (Exception e) {
            if (!realtime)
                console.stopLoader();
            console.error("Curl execution failed", e);
            String[] response = stdoutBuffer.toString().split("__HTTP_CODE__:");
            return new CurlResult(-1, response[0], stderrBuffer.toString(), Integer.parseInt(response[1]),
                    Duration.between(start, Instant.now()).toMillis());
        }
    }
}
