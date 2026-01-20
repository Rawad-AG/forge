package dev.forge.requester.http.curl;

import java.util.ArrayList;
import java.util.List;

import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.requester.http.HttpMethod;
import dev.forge.requester.http.request.Body;
import dev.forge.requester.http.request.Request;

public class CurlCompiler {
    private static final ForgeConfigLoader conf = ForgeEngine.context().config();

    public static CurlCommand compile(Request request) {
        List<String> args = new ArrayList<>();

        args.add("curl");

        if (request.method() != HttpMethod.GET) {
            args.add("-X");
            args.add(request.method().toString());
        }

        if (conf.hasPath("requester.headers")) {
            List<Object> defaultHeaders = conf.conf().getList("requester.headers").unwrapped();
            for (var h : defaultHeaders) {
                args.add("-H");
                args.add(h.toString());
            }
        }

        request.headers().forEach((name, values) -> {
            for (var value : values) {
                args.add("-H");
                args.add(name + ": " + value);
            }
        });

        if (request.body() != null) {
            if (request.body() instanceof Body.Inline inline) {
                args.add("-d");
                args.add(inline.content());
            } else if (request.body() instanceof Body.File file) {
                args.add("-d");
                args.add(file.path());
            }
        }

        args.add("-w");
        args.add("\\n__HTTP_CODE__:%{http_code}");

        args.add(request.url());

        return new CurlCommand(args);
    }

}
