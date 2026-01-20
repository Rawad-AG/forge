package dev.forge.engine.core;

import dev.forge.engine.cli.context.TerminalContext;
import dev.forge.engine.cli.input.Prompter;
import dev.forge.engine.cli.output.Console;
import dev.forge.engine.config.ForgeConfigLoader;

public class ExecutionContext {
    private ForgeConfigLoader config;
    private TerminalContext terminalCtx;
    private Console console;
    private Prompter prompter;

    public ExecutionContext(TerminalContext ctx, ForgeConfigLoader configuration) {
        config = configuration;
        terminalCtx = ctx;
        console = new Console(ctx);
        prompter = new Prompter(ctx);
    }

    public ForgeConfigLoader config() {
        return config;
    }

    public TerminalContext terminalCtx() {
        return terminalCtx;
    }

    public Console console() {
        return console;
    }

    public Prompter prompter() {
        return prompter;
    }

}