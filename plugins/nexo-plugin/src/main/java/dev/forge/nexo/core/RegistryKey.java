package dev.forge.nexo.core;

public enum RegistryKey {
    NONE,

    /* java.io.file object represents the json file to be parsed */
    FILE,

    /*
     * dev.forge.nexo.core.phases.parser.mapping.Root object represents the
     * json file after mapping
     */
    Parsed_Root

}
