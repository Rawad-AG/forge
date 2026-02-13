package dev.forge.nexo.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NexoContext {
    private final static Map<RegistryKey, DataBox> registry = new HashMap<>();

    public static void clear() {
        registry.clear();
    }

    public static DataBox computeIfAbsent(RegistryKey arg0, Function<? super RegistryKey, ? extends DataBox> arg1) {
        return registry.computeIfAbsent(arg0, arg1);
    }

    public static DataBox computeIfPresent(RegistryKey arg0,
            BiFunction<? super RegistryKey, ? super DataBox, ? extends DataBox> arg1) {
        return registry.computeIfPresent(arg0, arg1);
    }

    public static boolean containsKey(Object key) {
        return registry.containsKey(key);
    }

    public static boolean containsValue(Object value) {
        return registry.containsValue(value);
    }

    public static Set<Entry<RegistryKey, DataBox>> entrySet() {
        return registry.entrySet();
    }

    public static void forEach(BiConsumer<? super RegistryKey, ? super DataBox> action) {
        registry.forEach(action);
    }

    public static <T> T get(Object k) {
        if (registry.containsKey(k))
            return registry.get(k).get();

        throw new RuntimeException("required context item does not exists: " + k);
    }

    public static DataBox getOrDefault(Object arg0, DataBox arg1) {
        return registry.getOrDefault(arg0, arg1);
    }

    public static boolean isEmpty() {
        return registry.isEmpty();
    }

    public static Set<RegistryKey> keySet() {
        return registry.keySet();
    }

    public static DataBox put(RegistryKey arg0, DataBox arg1) {
        return registry.put(arg0, arg1);
    }

    public static void putAll(Map<? extends RegistryKey, ? extends DataBox> m) {
        registry.putAll(m);
    }

    public static DataBox putIfAbsent(RegistryKey arg0, DataBox arg1) {
        return registry.putIfAbsent(arg0, arg1);
    }

    public static int size() {
        return registry.size();
    }

    public static Collection<DataBox> values() {
        return registry.values();
    }

}
