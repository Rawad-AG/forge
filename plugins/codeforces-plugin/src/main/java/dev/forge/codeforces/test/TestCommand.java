package dev.forge.codeforces.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import picocli.CommandLine.Command;

@Command(name = "test", mixinStandardHelpOptions = true)
public class TestCommand implements Runnable {
    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        console.warn("Compiling and running your solution...");

        try {
            ProcessBuilder compilePb = new ProcessBuilder("javac", "Main.java");
            compilePb.inheritIO();
            Process compileProcess = compilePb.start();

            if (!compileProcess.waitFor(10, TimeUnit.SECONDS) || compileProcess.exitValue() != 0) {
                console.error("Compilation failed.");
                return;
            }

            ProcessBuilder runPb = new ProcessBuilder("java", "Main");

            File inputFile = new File("input.txt");
            if (inputFile.exists())
                runPb.redirectInput(inputFile);

            runPb.redirectErrorStream(true);

            Process process = runPb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null)
                    console.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0)
                console.warn("Process exited with non-zero code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            console.error("Execution Error: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
