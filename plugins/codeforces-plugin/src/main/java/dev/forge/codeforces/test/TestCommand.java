package dev.forge.codeforces.test;

import picocli.CommandLine.Command;

@Command(name = "test", mixinStandardHelpOptions = true)
public class TestCommand implements Runnable {

    @Override
    public void run() {
    }

}
