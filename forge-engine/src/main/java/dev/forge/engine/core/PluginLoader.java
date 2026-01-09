package dev.forge.engine.core;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import dev.forge.engine.config.ForgeConfigLoader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PluginLoader {

    private final String PLUGIN_DIR = System.getProperty("user.home") + "/.forge/plugins";
    private final String META_PATH = "META-INF/forge-plugin.properties";
    private final ForgeConfigLoader conf;

    /** Load a single plugin by name from the default plugin directory */
    public ForgePlugin loadPlugin(String pluginName) {
        File jarFile = findJarByName(pluginName);
        if (jarFile == null)
            return null;
        return loadPluginFromJar(jarFile);
    }

    /** Load all plugins from the default plugin directory */
    public List<ForgePlugin> loadAll() {
        List<ForgePlugin> plugins = new ArrayList<>();
        File dir = new File(PLUGIN_DIR);
        if (!dir.exists() || !dir.isDirectory())
            return plugins;

        File[] jars = dir.listFiles(f -> f.isFile() && f.getName().endsWith(".jar"));
        if (jars == null)
            return plugins;

        for (File jar : jars) {
            ForgePlugin plugin = loadPluginFromJar(jar);
            if (plugin != null)
                plugins.add(plugin);
        }

        return plugins;
    }

    /** Load a plugin from a specific JAR path */
    public ForgePlugin loadFromJar(Path path) {
        return loadPluginFromJar(path.toFile());
    }

    // ╔═════════════════════════════════════════════════════════════╗
    // ║ Internals
    // ╚═════════════════════════════════════════════════════════════╝
    private ForgePlugin loadPluginFromJar(File jar) {
        try (JarFile jarFile = new JarFile(jar)) {
            JarEntry meta = jarFile.getJarEntry(META_PATH);
            if (meta == null)
                throw new RuntimeException(jar.getName() + " does not provide metadata");

            Properties props = new Properties();
            try (InputStream is = jarFile.getInputStream(meta)) {
                props.load(is);
            }

            String entryPoint = props.getProperty("entryPoint");
            if (entryPoint == null) {
                throw new IllegalStateException("Invalid forge-plugin.properties in " + jar.getName());
            }

            URLClassLoader pluginClassLoader = new URLClassLoader(
                    new URL[] { jar.toURI().toURL() },
                    ForgeEngine.class.getClassLoader());

            Object instance = Class.forName(entryPoint, true, pluginClassLoader)
                    .getDeclaredConstructor()
                    .newInstance();

            if (!(instance instanceof ForgePlugin plugin)) {
                throw new IllegalStateException("Entry point does not implement ForgePlugin: " + entryPoint);
            }

            conf.parse(jarFile);
            return plugin;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load plugin from " + jar.getName(), e);
        }
    }

    private File findJarByName(String pluginName) {
        File dir = new File(PLUGIN_DIR);
        if (!dir.exists() || !dir.isDirectory())
            return null;

        File[] jars = dir.listFiles(f -> f.isFile() && f.getName().endsWith(".jar"));
        if (jars == null)
            return null;

        for (File jar : jars) {
            if (jar.getName().equals(pluginName + ".jar"))
                return jar;
        }

        return null;
    }
}
