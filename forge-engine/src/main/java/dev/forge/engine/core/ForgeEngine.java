package dev.forge.engine.core;

import java.util.Arrays;

import dev.forge.engine.cli.context.TerminalContext;
import dev.forge.engine.cli.output.Console;
import dev.forge.engine.config.ForgeConfigLoader;

public final class ForgeEngine {
    private static ForgeConfigLoader config;
    private static PluginLoader pluginLoader;
    private static ExecutionContext context;

    public static void main(String[] args) {
        TerminalContext ctx = TerminalContext.create();
        var console = new Console(ctx);

        if (args == null || args.length == 0)
            console.fatal("Usage forge <plugin> [...]");

        String target = args[0];
        ForgePlugin plugin = null;
        config = new ForgeConfigLoader();
        pluginLoader = new PluginLoader(config);
        if (config.hasPath("aliases." + target))
            target = config.getString("aliases." + target);

        try {
            plugin = pluginLoader.loadPlugin(target);

            if (plugin == null)
                console.fatal("Plugin not found: " + target);

            String[] pluginArgs = Arrays.copyOfRange(args, 1, args.length);

            context = new ExecutionContext(ctx, config);
            plugin.execute(pluginArgs);
        } catch (Exception e) {
            console.error(e.getMessage(), e);
        } finally {
            ctx.close();
            System.exit(0);
        }
    }

    public static ForgeConfigLoader config() {
        return config;
    }

    public static PluginLoader pluginLoader() {
        return pluginLoader;
    }

    public static ExecutionContext context() {
        return context;
    }

}
