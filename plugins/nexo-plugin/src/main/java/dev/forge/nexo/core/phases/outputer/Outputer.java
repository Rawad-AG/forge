package dev.forge.nexo.core.phases.outputer;

import java.util.List;

import dev.forge.engine.cli.output.Console;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;

public class Outputer implements Runnable {

    private final Console console = ForgeEngine.context().console();

    @Override
    public void run() {
        List<String> outputs = NexoContext.get(RegistryKey.Template_Outputs);

        console.info("=== Generated Files ===");
        for (int i = 0; i < outputs.size(); i++) {
            System.out.println("=====================================================");
            System.out.println(outputs.get(i));
        }
    }
}
