package dev.forge.requester.http.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Body.File.class, name = "file"),
        @JsonSubTypes.Type(value = Body.Inline.class, name = "inline")
})
public sealed interface Body permits Body.Inline, Body.File {

    record Inline(String content) implements Body {
    }

    record File(String path) implements Body {
    }
}
