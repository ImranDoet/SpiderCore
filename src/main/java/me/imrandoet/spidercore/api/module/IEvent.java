package me.imrandoet.spidercore.api.module;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface IEvent<T extends Event> extends Listener {

    public void event(T event);

}
