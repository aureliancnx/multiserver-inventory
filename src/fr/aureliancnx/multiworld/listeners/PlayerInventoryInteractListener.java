package fr.aureliancnx.multiworld.listeners;

import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;

public class PlayerInventoryInteractListener implements Listener
{
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getWhoClicked();
        if (PlayerCommandMultiworldListener.dontTouch.containsKey(player.getUniqueId()) && PlayerCommandMultiworldListener.dontTouch.get(player.getUniqueId()) >= System.currentTimeMillis()) {
            event.setCancelled(true);
            player.sendMessage("§cVous ne pouvez pas int\u00e9ragir pendant une t\u00e9l\u00e9portation.");
        }
    }
    
    @EventHandler
    public void onInventoryInteract(final InventoryInteractEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getWhoClicked();
        if (PlayerCommandMultiworldListener.dontTouch.containsKey(player.getUniqueId()) && PlayerCommandMultiworldListener.dontTouch.get(player.getUniqueId()) >= System.currentTimeMillis()) {
            event.setCancelled(true);
            player.sendMessage("§cVous ne pouvez pas int\u00e9ragir pendant une t\u00e9l\u00e9portation.");
        }
    }
}
