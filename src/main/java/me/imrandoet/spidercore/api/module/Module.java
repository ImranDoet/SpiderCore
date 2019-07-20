package me.imrandoet.spidercore.api.module;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public abstract class Module<T extends JavaPlugin> {

    private final T plugin;

    public Module(T plugin) {
        this.plugin = plugin;
    }

    public abstract void run();

    public T getInstance() {
        return plugin;
    }

    @Target(ElementType.TYPE)
    public @interface ModulePriority {

        int priority() default DEFAULT;

        int HIGHEST = 10;
        int DEFAULT = 5;
        int LOWEST = 1;

    }

}
