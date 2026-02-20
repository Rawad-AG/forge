package dev.forge.nexo.core.phases.templater;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import dev.forge.nexo.core.DataBox;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.modeler.models.JavaFileModel;

public class Templater implements Runnable {

    private final MustacheFactory mustacheFactory;

    public Templater() {
        this.mustacheFactory = new DefaultMustacheFactory("templates");
    }

    @Override
    public void run() {
        List<JavaFileModel> models = NexoContext.get(RegistryKey.Models);
        Map<JavaFileModel, String> outputs = new HashMap<>();

        for (JavaFileModel model : models) {
            try {
                String output = renderTemplate(model);
                outputs.put(model, output);
            } catch (Exception e) {
                throw new RuntimeException("Failed to render template for: " + model.getName(), e);
            }
        }

        NexoContext.put(RegistryKey.Template_Outputs, new DataBox(outputs));
    }

    private String renderTemplate(JavaFileModel model) {
        String templateName = getTemplateName(model);
        Mustache mustache = mustacheFactory.compile(templateName);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("model", model);

        StringWriter writer = new StringWriter();
        mustache.execute(writer, dataModel);

        return writer.toString();
    }

    private String getTemplateName(JavaFileModel model) {
        return "entity.mustache";
    }
}