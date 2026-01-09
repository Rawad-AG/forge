package dev.forge.make.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ProjectUtils {
    public static String getRootPackage() {
        Path srcRoot = Path.of(System.getProperty("user.dir"), "src", "main", "java");
        if (!Files.isDirectory(srcRoot))
            return null;

        List<String[]> packages = new ArrayList<>();

        try (var paths = Files.walk(srcRoot)) {
            paths.filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".java"))
                    .forEach(p -> extractPackage(p).ifPresent(pkg -> packages.add(pkg.split("\\."))));
        } catch (IOException e) {
            throw new RuntimeException("Failed to scan source files", e);
        }

        if (packages.isEmpty())
            return null;

        String[] base = packages.get(0);
        int commonLength = base.length;

        for (int i = 1; i < packages.size(); i++) {
            String[] current = packages.get(i);
            commonLength = Math.min(commonLength, current.length);

            for (int j = 0; j < commonLength; j++)
                if (!base[j].equals(current[j])) {
                    commonLength = j;
                    break;
                }

            if (commonLength == 0)
                break;
        }

        if (commonLength == 0)
            return null;

        return String.join(".", Arrays.copyOf(base, commonLength));
    }

    public static Document parsePom() {
        try {
            File pomFile = new File(System.getProperty("user.dir") + "/pom.xml");
            if (!pomFile.exists()) {
                throw new RuntimeException("pom.xml not found in current directory.");
            }

            var dbFactory = DocumentBuilderFactory.newInstance();
            var dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(pomFile);
            doc.getDocumentElement().normalize();

            return doc;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    // *============================================================
    // *= Helper Functions
    // *============================================================
    private static Optional<String> extractPackage(Path javaFile) {
        try (BufferedReader reader = Files.newBufferedReader(javaFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.startsWith("package "))
                    continue;

                int semicolon = line.indexOf(';');
                if (semicolon == -1)
                    return Optional.empty();

                return Optional.of(line.substring(8, semicolon).trim());
            }
        } catch (IOException ignored) {
        }

        return Optional.empty();
    }

}
