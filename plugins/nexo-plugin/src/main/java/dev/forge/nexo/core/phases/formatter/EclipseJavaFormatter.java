package dev.forge.nexo.core.phases.formatter;

import java.util.Map;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public final class EclipseJavaFormatter {

    private static final CodeFormatter FORMATTER = ToolFactory.createCodeFormatter(Map.of());

    public static String format(String source) {
        TextEdit edit = FORMATTER.format(
                CodeFormatter.K_COMPILATION_UNIT,
                source,
                0,
                source.length(),
                0,
                System.lineSeparator());

        if (edit == null)
            return source;

        try {
            Document doc = new Document(source);
            edit.apply(doc);
            return doc.get();
        } catch (Exception e) {
            return source;
        }
    }
}
