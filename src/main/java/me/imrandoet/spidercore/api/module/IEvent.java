package me.imrandoet.spidercore.api.module;

import org.bukkit.event.Event;

public interface IEvent<T extends Event> {

    public void event(T event);

}
