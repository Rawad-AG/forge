package dev.forge.nexo.core.phases.model;

public interface ModelBuilder<T> {
    void build();
    T getModel();
}
