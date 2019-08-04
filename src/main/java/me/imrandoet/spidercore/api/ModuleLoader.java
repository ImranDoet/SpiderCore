package me.imrandoet.spidercore.api;

import me.imrandoet.spidercore.api.module.IDisableable;
import me.imrandoet.spidercore.api.module.Module;
import me.imrandoet.spidercore.api.module.Module.ModulePriority;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModuleLoader<T> {

    private final T javaPlugin;

    private final Map<Class, Module> modules;

    private final String pluginPackage;

    public ModuleLoader(T javaPlugin, String pluginPackage) {
        this.javaPlugin = javaPlugin;
        this.modules = new LinkedHashMap<>();
        this.pluginPackage = pluginPackage;
    }

    public void loadModules() {
        LinkedHashMap<Module, Integer> moduleIntegerLinkedHashMap = new LinkedHashMap<>();
        scanForModules(clazz -> {
            try {
                Module module = (Module) clazz.getDeclaredConstructor(javaPlugin.getClass()).newInstance(javaPlugin);

                modules.put(clazz, module);
                moduleIntegerLinkedHashMap.put(module, ((ModulePriority) (clazz.getAnnotation(ModulePriority.class))).priority());

                module.getEvents().forEach(o -> Bukkit.getPluginManager().registerEvents((Listener) o, (JavaPlugin) javaPlugin));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        });


        //Start sorting of modules
        LinkedHashMap<Module, Integer> sorted = moduleIntegerLinkedHashMap.entrySet()
                .stream()
                .sorted((Map.Entry.<Module, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        modules.clear();
        sorted.forEach((module, integer) -> modules.put(module.getClass(), module));

        modules.forEach((key, value) -> value.run());
    }

    public void disableModules() {
        modules.values().forEach(module -> {
            if (module.getClass().isAssignableFrom(IDisableable.class)) {
                ((IDisableable) module).disable();
            }
        });
    }

    private void scanForModules(Consumer<? super Class> consumer) {
        Reflections reflections = new Reflections(pluginPackage);

        Set<Class<? extends Module>> allClasses = reflections.getSubTypesOf(Module.class);

        for (Class<? extends Object> clazz : allClasses) {
            consumer.accept(clazz);
        }
    }

    public <T extends Module> T getModule(Class<T> type) {
        return type.cast(modules.get(type));
    }

}
