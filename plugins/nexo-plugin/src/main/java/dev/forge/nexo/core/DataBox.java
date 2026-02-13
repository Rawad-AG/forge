package dev.forge.nexo.core;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DataBox {
    private final Object data;

    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) data;
    }
}
