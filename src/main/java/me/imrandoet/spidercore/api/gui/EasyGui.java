package me.imrandoet.spidercore.api.gui;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class EasyGui implements IEasyGUI {

    @Getter
    protected LinkedHashMap<InventoryItem, Integer> items;

    @Getter
    private Inventory inventory;

    private Consumer<InventoryCloseEvent> inventoryCloseEventConsumer;

    private EasyGui(Inventory inventory, LinkedHashMap<InventoryItem, Integer> items) {
        this.inventory = inventory;
        this.items = items;

        //Fill inventory
        this.items.entrySet().forEach(inventoryItemIntegerEntry -> inventory.setItem(inventoryItemIntegerEntry.getValue(), inventoryItemIntegerEntry.getKey().getItemStack()));
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void setInventoryCloseEventConsumer(Consumer<InventoryCloseEvent> closeEventConsumer) {
        this.inventoryCloseEventConsumer = closeEventConsumer;
    }


    public static class EasyGuiBuilder {

        private Inventory inventory;
        private String title;
        private int size;

        private LinkedHashMap<InventoryItem, Integer> items;

        private BiConsumer<InventoryCloseEvent, Player> closeEventConsumer;
        private BiConsumer<InventoryOpenEvent, Player> openEventConsumer;

        public static EasyGuiBuilder create() {
            return new EasyGuiBuilder();
        }

        public EasyGuiBuilder title(String name) {
            this.title = name;
            return this;
        }

        public EasyGuiBuilder close(BiConsumer<InventoryCloseEvent, Player> eventConsumer) {
            this.closeEventConsumer = eventConsumer;
            return this;
        }

        public EasyGuiBuilder open(BiConsumer<InventoryOpenEvent, Player> eventConsumer) {
            this.openEventConsumer = eventConsumer;
            return this;
        }

        public EasyGuiBuilder item(InventoryItem item) {
            this.items.put(item, item.getSlot());
            return this;
        }

        public EasyGuiBuilder size(int size) {
            this.size = size;
            return this;
        }

        public EasyGui build() {
            return new EasyGui(Bukkit.createInventory(new EasyHolder(), size, title), items) {
                @Override
                public BiConsumer<InventoryCloseEvent, Player> onClose() {
                    return closeEventConsumer;
                }

                @Override
                public BiConsumer<InventoryOpenEvent, Player> onOpen() {
                    return openEventConsumer;
                }
            };
        }
    }

    public static class EasyHolder implements InventoryHolder {

        private Inventory inventory;

        private UUID uuid = UUID.randomUUID();

        public UUID getUuid() {
            return uuid;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }
}
