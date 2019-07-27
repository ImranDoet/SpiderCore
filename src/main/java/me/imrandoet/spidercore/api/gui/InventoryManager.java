package me.imrandoet.spidercore.api.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InventoryManager implements Listener {

    private final JavaPlugin plugin;

    private HashMap<UUID, EasyGui> openInventories;

    private ArrayList<EasyGui> inventories;

    public InventoryManager(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;

        this.openInventories = new HashMap<>();
        this.inventories = new ArrayList<>();

        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    public void registerInventory(EasyGui easyGui) {
        this.inventories.add(easyGui);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (this.openInventories.containsKey(player.getUniqueId())) {
            EasyGui easyGui = this.openInventories.get(player.getUniqueId());

            if (!(event.getInventory().getHolder() instanceof EasyGui.EasyHolder)) return;

            if (((EasyGui.EasyHolder) easyGui.getInventory().getHolder()).getUuid() != ((EasyGui.EasyHolder) event.getInventory()).getUuid()) return;

            easyGui.onClose().accept(event, player);

        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if (this.openInventories.containsKey(player.getUniqueId())) {
            EasyGui easyGui = this.openInventories.get(player.getUniqueId());

            if (!(event.getInventory().getHolder() instanceof EasyGui.EasyHolder)) return;

            if (((EasyGui.EasyHolder) easyGui.getInventory().getHolder()).getUuid() != ((EasyGui.EasyHolder) event.getInventory()).getUuid()) return;

            easyGui.onOpen().accept(event, player);

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (this.openInventories.containsKey(player.getUniqueId())) {
            EasyGui easyGui = this.openInventories.get(player.getUniqueId());

            if (!(event.getInventory().getHolder() instanceof EasyGui.EasyHolder)) return;

            if (((EasyGui.EasyHolder) easyGui.getInventory().getHolder()).getUuid() != ((EasyGui.EasyHolder) event.getInventory()).getUuid()) return;

            if (event.getAction() == InventoryAction.NOTHING) {
                event.setCancelled(true);
                return;
            }

            easyGui.items.entrySet().stream()
                    .filter(inventoryItemIntegerEntry -> inventoryItemIntegerEntry.getKey().getItemStack().equals(event.getCurrentItem()) && inventoryItemIntegerEntry.getValue() == event.getSlot())
                    .forEach(inventoryItemIntegerEntry -> inventoryItemIntegerEntry.getKey().getConsumer().accept(event));
        }
    }

}
