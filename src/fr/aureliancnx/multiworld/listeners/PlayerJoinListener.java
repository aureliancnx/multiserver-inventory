package fr.aureliancnx.multiworld.listeners;

import org.bukkit.event.EventHandler;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import fr.aureliancnx.multiworld.rabbit.packets.PlayerCommand;
import fr.aureliancnx.multiworld.rabbit.listeners.ReceiveCommandListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import fr.aureliancnx.multiworld.Multiworld;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener
{
    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (PlayerLoginListener.newJoiners.containsKey(player.getUniqueId()) && PlayerLoginListener.newJoiners.get(player.getUniqueId()) > System.currentTimeMillis()) {
            Bukkit.getScheduler().runTaskLater((Plugin)Multiworld.instance, (Runnable)new Runnable() {
                @Override
                public void run() {
                    PlayerLoginListener.newJoiners.remove(player.getUniqueId());
                    Bukkit.broadcastMessage("§e§oBienvenue \u00e0 §7§o" + player.getName() + " §e§osur le Survie.");
                }
            }, 20L);
        }
        if (ReceiveCommandListener.tmpCommandData.containsKey(player.getName())) {
            final PlayerCommand playerCommand = ReceiveCommandListener.tmpCommandData.get(player.getName());
            if (playerCommand.isExpired()) {
                ReceiveCommandListener.tmpCommandData.remove(player.getName());
                Logs.log("[AsgardiaMultiworld] [" + player.getName() + "] Removed expired command order.", true);
                return;
            }
            Bukkit.getScheduler().runTaskLater((Plugin)Multiworld.instance, (Runnable)new Runnable() {
                @Override
                public void run() {
                    player.performCommand(playerCommand.command);
                    Logs.log("[AsgardiaMultiworld] [" + player.getName() + "] Executed command order: " + playerCommand.command, true);
                    ReceiveCommandListener.tmpCommandData.remove(player.getName());
                }
            }, 1L);
        }
    }
}
