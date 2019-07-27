package me.imrandoet.spidercore.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.function.BiConsumer;

public interface IEasyGUI {

    BiConsumer<InventoryCloseEvent, Player> onClose();

    BiConsumer<InventoryOpenEvent, Player> onOpen();

}
