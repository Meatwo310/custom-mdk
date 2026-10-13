package net.meatwo310.examplemod.mdk.config;

/**
 * Loader-neutral config types. Names follow NeoForge's local/synced terminology.
 * Loader adapters preserve the target loader's storage and synchronization behavior.
 */
public enum ConfigSide {
    /**
     * Installation-local settings loaded independently on both physical sides.
     * These values are not synchronized between server and client.
     * Formerly named {@code COMMON}.
     */
    LOCAL,

    /**
     * Server-owned settings synchronized to connected clients.
     * Do not store secrets here: clients receive these values.
     * Formerly named {@code SERVER}.
     */
    SYNCED,

    /** Client-only preferences that are not synchronized to the server. */
    CLIENT;

    /**
     * Compatibility alias for {@link #LOCAL}; not an enum constant.
     *
     * @deprecated Use {@link #LOCAL}. Retained for compatibility with ordinary field references.
     */
    @Deprecated(forRemoval = false)
    public static final ConfigSide COMMON = LOCAL;

    /**
     * Compatibility alias for {@link #SYNCED}; not an enum constant.
     *
     * @deprecated Use {@link #SYNCED}. Retained for compatibility with ordinary field references.
     */
    @Deprecated(forRemoval = false)
    public static final ConfigSide SERVER = SYNCED;
}
