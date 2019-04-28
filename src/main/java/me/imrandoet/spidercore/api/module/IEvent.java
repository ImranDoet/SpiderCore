package me.imrandoet.spidercore.api.module;

import org.bukkit.event.Event;

public interface IEvent<T extends Event> {

    public <T> void event(T event);

}
