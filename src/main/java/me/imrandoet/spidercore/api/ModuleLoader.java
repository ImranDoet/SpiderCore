package me.imrandoet.spidercore.api;

import me.imrandoet.spidercore.api.module.IDisableable;
import me.imrandoet.spidercore.api.module.IEvent;
import me.imrandoet.spidercore.api.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ModuleLoader<T> {

    private final T javaPlugin;

    private final Map<Class, Module> modules;

    private final String pluginPackage;

    public ModuleLoader(T javaPlugin, String pluginPackage) {
        this.javaPlugin = javaPlugin;
        this.modules = new HashMap<>();
        this.pluginPackage = pluginPackage;

        loadModules();
    }

    private void loadModules() {
        scanForModules(clazz -> {
            try {
                Module module = (Module) clazz.getDeclaredConstructor(javaPlugin.getClass()).newInstance(javaPlugin);

                modules.put(clazz, module);

                if (module instanceof IEvent) {
                    Bukkit.getPluginManager().registerEvents((IEvent) module, (JavaPlugin) javaPlugin);
                }

            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
                //Custom logging system
                exception.printStackTrace();
            }
        });
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

        for (Class clazz : allClasses) {
            consumer.accept(clazz);
        }
    }

    public <T extends Module> T getModule(Class<T> type) {
        return type.cast(modules.get(type));
    }

}
