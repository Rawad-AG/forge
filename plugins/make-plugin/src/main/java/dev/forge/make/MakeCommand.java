package dev.forge.make;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.make.controller.MakeController;
import dev.forge.make.entity.MakeEntity;
import dev.forge.make.repo.MakeRepository;
import dev.forge.make.service.MakeService;
import lombok.Setter;
import picocli.CommandLine.Command;

@Setter
@Command(name = "make", mixinStandardHelpOptions = true, subcommands = {
        MakeController.class,
        MakeEntity.class,
        MakeRepository.class,
        MakeService.class,
        MakeAll.class })
public class MakeCommand implements Runnable {

    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        console.fatal("Usage: forge make <command> [...options]");
    }

}
