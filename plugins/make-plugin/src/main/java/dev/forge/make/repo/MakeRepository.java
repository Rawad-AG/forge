package dev.forge.make.repo;

import dev.forge.engine.utils.StringUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "repository", aliases = "repo", mixinStandardHelpOptions = true)
public class MakeRepository implements Runnable {
    @Parameters(index = "0")
    private String name;

    @Option(names = { "--override", "--force", "-f" })
    private boolean override = false;

    @Option(names = { "--pkg", "-p" })
    private String pkg;

    @Option(names = { "--template", "-t" })
    private String template;

    @Override
    public void run() {
        name = StringUtils.capitalize(name);
        RepositoryMaker.make(pkg, name, template, override);
    }
}
