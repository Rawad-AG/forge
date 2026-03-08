package dev.forge.codeforces;

import dev.forge.codeforces.search.SearchCommand;
import dev.forge.codeforces.submit.SubmitCommand;
import dev.forge.codeforces.test.TestCommand;
import picocli.CommandLine.Command;

@Command(name = "codeforces", mixinStandardHelpOptions = true, subcommands = {
        SearchCommand.class,
        TestCommand.class,
        SubmitCommand.class
})
public class CodeforcesCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello World!.");
    }
}
