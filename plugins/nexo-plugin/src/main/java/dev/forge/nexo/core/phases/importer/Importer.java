package dev.forge.nexo.core.phases.importer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.forge.nexo.core.NexoContext;
import dev.forge.nexo.core.RegistryKey;
import dev.forge.nexo.core.phases.modeler.models.ClassModel;
import dev.forge.nexo.core.phases.modeler.models.InterfaceModel;
import dev.forge.nexo.core.phases.modeler.models.JavaFileModel;
import dev.forge.nexo.core.phases.modeler.models.RecordModel;
import dev.forge.nexo.core.phases.modeler.models.field.JavaTypeModel;
import dev.forge.nexo.utils.ImportsRepo;
import lombok.NonNull;

public class Importer implements Runnable {

    private Set<String> added = new HashSet<>();

    @Override
    public void run() {
        List<JavaFileModel> models = NexoContext.get(RegistryKey.Models);

        models.forEach(this::organizeImports);

    }

    private void organizeImports(JavaFileModel model) {
        added.clear();
        for (var a : model.getAnnotations())
            addImport(model, a.getName());

        for (var a : model.getImplementedInterfaces())
            addImport(model, a);

        if (model instanceof ClassModel clazz) {
            for (var f : clazz.getFields())
                addImport(model, f.getType());

            for (var m : clazz.getMethods()) {
                addImport(model, m.getReturnType());

                for (var p : m.getParameters())
                    addImport(model, p.type());
            }
        }

        else if (model instanceof InterfaceModel interfaze)
            for (var m : interfaze.getMethods()) {
                addImport(model, m.getReturnType());

                for (var p : m.getParameters())
                    addImport(model, p.type());
            }

        else if (model instanceof RecordModel rcd)
            for (var f : rcd.getComponents())
                addImport(model, f.getType());

    }

    private void addImport(JavaFileModel model, @NonNull JavaTypeModel type) {
        addImport(model, type.type());
        if (type.generic() != null)
            addImport(model, type.generic());
    }

    private void addImport(JavaFileModel model, String t) {
        if (t == null)
            return;

        String imp = ImportsRepo.get(t);

        if (imp == null)
            return;

        if (added.contains(imp))
            return;

        model.getImports().add(imp);
        added.add(imp);
    }

}
