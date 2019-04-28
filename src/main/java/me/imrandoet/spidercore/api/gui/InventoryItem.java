package me.imrandoet.spidercore.api.gui;

import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Data
public class InventoryItem {

    private ItemStack itemStack;

    private int slot;

    private Consumer<InventoryClickEvent> consumer;

    private InventoryItem(ItemStack itemStack, Consumer<InventoryClickEvent> clickEventConsumer, int slot) {
        this.itemStack = itemStack;
        this.consumer = clickEventConsumer;
    }

    public void onClick(InventoryClickEvent event) {
        this.consumer.accept(event);
    }

    public static InventoryItem of(ItemStack stack, Consumer<InventoryClickEvent> clickEventConsumer, int slot) {
        return new InventoryItem(stack, clickEventConsumer, slot);
    }

}
