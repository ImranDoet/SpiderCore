package me.imrandoet.spidercore.api;

import me.imrandoet.spidercore.api.module.IDisableable;
import me.imrandoet.spidercore.api.module.Module;
import me.imrandoet.spidercore.api.module.Module.ModulePriority;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ModuleLoader<T> {

    private final T javaPlugin;

    private final Map<Class, Module> modules;
    private final LinkedHashMap<Class, Module> sortedModules;

    private final String pluginPackage;

    public ModuleLoader(T javaPlugin, String pluginPackage) {
        this.javaPlugin = javaPlugin;
        this.modules = new LinkedHashMap<>();
        this.sortedModules = new LinkedHashMap<>();
        this.pluginPackage = pluginPackage;

        loadModules();
    }

    private void loadModules() {
        scanForModules(clazz -> {
            try {
                Module module = (Module) clazz.getDeclaredConstructor(javaPlugin.getClass()).newInstance(javaPlugin);

                modules.put(clazz, module);

                module.getEvents().forEach(o -> Bukkit.getPluginManager().registerEvents((Listener) o, (JavaPlugin) javaPlugin));

            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
                //Custom logging system
                exception.printStackTrace();
            }
        });
        modules.entrySet().stream().sorted(Comparator.comparingInt(o -> ((ModulePriority) o.getKey().getAnnotation(ModulePriority.class)).priority())).forEachOrdered(classModuleEntry -> sortedModules.put(classModuleEntry.getKey(), classModuleEntry.getValue()));

        this.sortedModules.entrySet().stream().forEach(classModuleEntry -> classModuleEntry.getValue().run());
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
