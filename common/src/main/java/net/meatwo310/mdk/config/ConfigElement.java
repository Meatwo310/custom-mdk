package net.meatwo310.mdk.config;

interface ConfigElement {
    void bindTo(ConfigVisitor visitor);
}
