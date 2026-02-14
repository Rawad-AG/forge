package dev.forge.nexo.core.phases.templater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.forge.nexo.core.DataBox;
import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.modeler.models.JavaFileModel;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class Templater implements Runnable {

    private final Configuration freemarkerConfig;

    public Templater() {
        this.freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);
        this.freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
    }

    @Override
    public void run() {
        List<JavaFileModel> models = NexoContext.get(RegistryKey.Entity_Models);
        List<String> outputs = new ArrayList<>();

        for (JavaFileModel model : models) {
            try {
                String output = renderTemplate(model);
                outputs.add(output);
            } catch (Exception e) {
                throw new RuntimeException("Failed to render template for: " + model.getName(), e);
            }
        }

        NexoContext.put(RegistryKey.Template_Outputs, new DataBox(outputs));
    }

    private String renderTemplate(JavaFileModel model) throws Exception {
        String templateName = getTemplateName(model);
        Template template = freemarkerConfig.getTemplate(templateName);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("model", model);

        StringWriterPlus writer = new StringWriterPlus();
        template.process(dataModel, writer);

        return writer.toString();
    }

    private String getTemplateName(JavaFileModel model) {
        return "entity.ftl";
    }
}
