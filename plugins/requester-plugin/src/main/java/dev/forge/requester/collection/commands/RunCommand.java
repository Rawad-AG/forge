package dev.forge.requester.collection.commands;

import java.nio.file.Path;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.collection.json.collection.CollectionItem;
import dev.forge.requester.collection.utils.CollectionLoader;
import dev.forge.requester.http.request.Request;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "run", aliases = { "fire" }, mixinStandardHelpOptions = true)
public class RunCommand implements Runnable {
    @Option(names = { "--realtime", "-r" }, description = "prints real time status", defaultValue = "true")
    boolean realtime;

    @Parameters(index = "0", description = "the id of the request to fire")
    String itemId;

    @Option(names = "--dir", required = false)
    String dir;

    private final Console console = ForgeEngine.context().console();
    private final ForgeConfigLoader conf = ForgeEngine.context().config();

    @Override
    public void run() {
        try {
            if (dir == null || dir.isBlank())
                dir = conf.getString("requester.collections.directory");

            var loader = new CollectionLoader(Path.of(dir));
            loader.loadAll();

            var item = loader.getItem(itemId);
            if (item == null || !(item instanceof CollectionItem.Request))
                throw new RuntimeException("request not found");

            var req = (CollectionItem.Request) item;
            Request request = new Request(req.method(), req.url(), req.headers(), req.body());

            request.fire(realtime);
        } catch (Exception e) {
            console.error("Error:", e);
        }
    }

}
