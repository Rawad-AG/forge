package dev.forge.archetype;

import java.nio.file.Path;

import dev.forge.archetype.generators.Generator;
import dev.forge.engine.cli.input.Prompter;
import dev.forge.engine.core.ForgeEngine;
import lombok.Setter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Setter
@Command(name = "archetype", description = "Create a new plugin")
public class ArchetypeCommand implements Runnable {

    @Option(names = "--dir", defaultValue = ".", description = "creation directory")
    private String dir;

    @Parameters(index = "0", arity = "0..1", description = "the template you want to create")
    private String template;

    private final Prompter prompter = ForgeEngine.context().prompter();

    @Override
    public void run() {
        if (template == null || template.isBlank())
            template = prompter.askRequired("enter the template");

        new Generator().generate(Path.of(dir), template);
    }

}
