package net.meatwo310.examplemod.mdk.config;

interface ConfigElement {
    void bindTo(ConfigVisitor visitor);
}
