package me.imrandoet.spidercore.api.module;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public interface IEvent<T extends Event> extends Listener {

    @EventHandler
    void event(T event);

}
