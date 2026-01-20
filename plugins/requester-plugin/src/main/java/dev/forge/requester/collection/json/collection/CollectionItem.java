package dev.forge.requester.collection.json.collection;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import dev.forge.requester.collection.json.MetaData;
import dev.forge.requester.http.HttpMethod;
import dev.forge.requester.http.request.Body;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CollectionItem.Folder.class, name = "folder"),
        @JsonSubTypes.Type(value = CollectionItem.Request.class, name = "request")
})
public sealed interface CollectionItem permits CollectionItem.Folder, CollectionItem.Request {
    public MetaData meta();

    record Folder(MetaData meta, List<CollectionItem> items) implements CollectionItem {
    }

    record Request(
            MetaData meta,
            HttpMethod method,
            String url,
            Map<String, List<String>> headers,
            Body body,
            RequestOptions options) implements CollectionItem {
    }
}
