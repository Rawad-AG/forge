package dev.forge.make.service;

import dev.forge.engine.utils.StringUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "service", mixinStandardHelpOptions = true)
public class MakeService implements Runnable {
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
        new ServiceMaker().make(pkg, name, template, override);

    }
}
