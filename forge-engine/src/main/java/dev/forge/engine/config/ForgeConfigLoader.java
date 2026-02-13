package dev.forge.engine.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;

public final class ForgeConfigLoader {

    private Config conf;

    public Config conf() {
        return conf;
    }

    public ForgeConfigLoader() {
        Path currentDirConf = Path.of(".").resolve("forge.conf");
        Config fileConf = currentDirConf.toFile().exists()
                ? ConfigFactory.parseFile(currentDirConf.toFile())
                : ConfigFactory.empty();

        Path userHomeConfPath = Path.of(System.getProperty("user.home")).resolve(".forge").resolve("forge.conf");
        Config userHomeConf = userHomeConfPath.toFile().exists()
                ? ConfigFactory.parseFile(userHomeConfPath.toFile())
                : ConfigFactory.empty();

        Config engineConf = ConfigFactory.parseResources("application.conf", ConfigParseOptions.defaults());

        this.conf = fileConf
                .withFallback(userHomeConf)
                .withFallback(engineConf)
                .resolve();
    }

    public void parse(JarFile jar) {
        JarEntry entry = jar.getJarEntry("application.conf");
        if (entry == null)
            return;

        try (InputStream is = jar.getInputStream(entry)) {
            Config pluginConf = ConfigFactory.parseReader(
                    new InputStreamReader(is),
                    ConfigParseOptions.defaults());

            conf = conf.withFallback(pluginConf).resolve();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load application.conf from " + jar.getName(), e);
        }
    }

    public boolean hasPath(String path) {
        return conf.hasPath(path);
    }

    public boolean hasPathOrNull(String path) {
        return conf.hasPathOrNull(path);
    }

    public boolean getBoolean(String path) {
        return conf.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return hasPath(path) ? getBoolean(path) : def;
    }

    public int getInt(String path) {
        return conf.getInt(path);
    }

    public int getInt(String path, int def) {
        return hasPath(path) ? getInt(path) : def;
    }

    public long getLong(String path) {
        return conf.getLong(path);
    }

    public long getLong(String path, long def) {
        return hasPath(path) ? getLong(path) : def;
    }

    public double getDouble(String path) {
        return conf.getDouble(path);
    }

    public double getDouble(String path, double def) {
        return hasPath(path) ? getDouble(path) : def;
    }

    public String getString(String path) {
        return conf.getString(path);
    }

    public String getString(String path, String def) {
        return hasPath(path) ? getString(path) : def;
    }

    public String getPath(String path) {
        return resolvePath(conf.getString(path));
    }

    public String getPath(String path, String def) {
        return hasPath(path) ? getPath(conf.getString(path)) : resolvePath(def);
    }

    // ╔═════════════════════════════════════════════════════════════╗
    // ║ Internals
    // ╚═════════════════════════════════════════════════════════════╝
    private String resolvePath(String path) {
        return path.replaceAll("~", System.getProperty("user.home"));
    }

}
