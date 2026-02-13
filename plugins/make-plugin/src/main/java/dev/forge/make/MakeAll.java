package dev.forge.make;

import dev.forge.engine.utils.StringUtils;
import dev.forge.make.controller.ControllerMaker;
import dev.forge.make.entity.EntityMaker;
import dev.forge.make.repo.RepositoryMaker;
import dev.forge.make.service.ServiceMaker;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "all", mixinStandardHelpOptions = true)
public class MakeAll implements Runnable {

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
        new RepositoryMaker().make(pkg, name, template, override);
        new ServiceMaker().make(pkg, name, template, override);
        new ControllerMaker().make(pkg, name, template, override);
        new EntityMaker().make(pkg, name, template, override);
    }
}
