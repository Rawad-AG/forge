package dev.forge.requester.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.http.curl.CurlCommand;
import dev.forge.requester.http.curl.CurlCompiler;
import dev.forge.requester.http.request.Request;
import dev.forge.requester.http.request.RequestParser;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "http", mixinStandardHelpOptions = true)
public class HttpCommand implements Runnable {
    @Parameters(arity = "1..2", paramLabel = "[METHOD] URL")
    List<String> positionals;

    @Option(names = { "--header", "-h" }, description = "HTTP header, e.g. 'Key: Value'")
    List<String> headers = new ArrayList<>();

    @Option(names = { "--body", "-d", "-b" }, description = "Request body or file")
    String body;

    @Option(names = "--env", description = "add an environment variable")
    List<String> env = new ArrayList<>();

    @Option(names = { "--realtime", "-r" }, description = "prints real time status", defaultValue = "true")
    boolean realtime;

    @Option(names = "--dry", description = "prints the curl command without executing it", defaultValue = "false")
    boolean dry;

    @Option(names = "--profile", description = "the profile or environment to activate", defaultValue = "default")
    String profile;

    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        try {
            Request request = RequestParser.parse(positionals, headers, body, parseEnv());

            if (dry) {
                CurlCommand curl = CurlCompiler.compile(request);
                curl.args().forEach(a -> console.println("    " + a + " \\"));
            } else
                request.fire(realtime);

        } catch (Exception e) {
            console.error(e.getMessage(), e);
        }
    }

    private Map<String, String> parseEnv() {
        Map<String, String> environment = new HashMap<>();

        var conf = ForgeEngine.context().config();
        if (conf.hasPath("requester.env." + profile)) {
            for (var entry : conf.conf().getConfig("requester.env." + profile).root().entrySet())
                environment.put(entry.getKey(), entry.getValue().unwrapped().toString());
        }

        env.forEach(v -> {
            String[] parts = v.split(":");
            if (parts.length < 2)
                console.fatal("invalid env variable syntax: " + v + " variables should be 'key:value'");

            environment.put(parts[0], String.join(":", Arrays.copyOfRange(parts, 1, parts.length)));
        });

        return environment;
    }
}
