package dev.forge.requester.http.request;

import java.util.List;
import java.util.Map;

import dev.forge.requester.http.HttpMethod;
import dev.forge.requester.http.curl.CurlCommand;
import dev.forge.requester.http.curl.CurlCompiler;
import dev.forge.requester.http.curl.CurlExecutor;
import dev.forge.requester.http.curl.CurlResult;
import dev.forge.requester.http.response.ResponsePrinter;

public record Request(
        HttpMethod method,
        String url,
        Map<String, List<String>> headers,
        Body body) {

    public CurlResult fire(boolean realtime) {
        CurlCommand curl = CurlCompiler.compile(this);

        CurlResult result = CurlExecutor.execute(curl, realtime);
        ResponsePrinter.print(result, realtime);
        return result;
    }

}
