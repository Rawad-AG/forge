package dev.forge.nexo.core.phases.formatter;

import java.util.HashMap;
import java.util.Map;

import dev.forge.nexo.core.DataBox;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.modeler.models.JavaFileModel;

public class Formatter implements Runnable {

    @Override
    public void run() {
        Map<JavaFileModel, String> rawFiles = NexoContext.get(RegistryKey.Template_Outputs);
        Map<JavaFileModel, String> formattedFiles = new HashMap<>();
        for (var entry : rawFiles.entrySet()) {
            formattedFiles.put(entry.getKey(), EclipseJavaFormatter.format(entry.getValue()));
        }

        NexoContext.put(RegistryKey.Template_Outputs, new DataBox(formattedFiles));
    }

}
