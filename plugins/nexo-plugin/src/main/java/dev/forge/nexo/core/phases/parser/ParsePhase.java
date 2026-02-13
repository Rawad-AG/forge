package dev.forge.nexo.core.phases.parser;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.forge.nexo.core.DataBox;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.parser.mapping.Root;

public class ParsePhase implements Runnable {

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        File file = NexoContext.get(RegistryKey.FILE);
        try {
            var root = mapper.readValue(file, Root.class);
            NexoContext.put(RegistryKey.Parsed_Root, new DataBox(root));
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse json file", e);
        }
    }

}
