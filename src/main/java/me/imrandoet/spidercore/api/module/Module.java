package me.imrandoet.spidercore.api.module;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

public abstract class Module<T extends JavaPlugin> {

    @Getter
    private Set<IEvent> events;

    private final T plugin;

    public Module(T plugin) {
        this.plugin = plugin;
        this.events = new HashSet<>();
    }

    public abstract void run();

    public T getInstance() {
        return plugin;
    }

    public void registerListener(IEvent iEvent) {
        events.add(iEvent);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ModulePriority {

        int priority() default DEFAULT;

        int HIGHEST = 3;
        int DEFAULT = 2;
        int LOWEST = 1;

    }

}
