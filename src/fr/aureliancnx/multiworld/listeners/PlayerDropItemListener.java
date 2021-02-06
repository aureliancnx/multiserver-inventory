package fr.aureliancnx.multiworld.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.Listener;

public class PlayerDropItemListener implements Listener
{
    @EventHandler
    public void onDropItem(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (PlayerCommandMultiworldListener.dontTouch.containsKey(player.getUniqueId()) && PlayerCommandMultiworldListener.dontTouch.get(player.getUniqueId()) >= System.currentTimeMillis()) {
            event.setCancelled(true);
            player.sendMessage("§cVous ne pouvez pas int\u00e9ragir pendant une t\u00e9l\u00e9portation.");
        }
    }
}
