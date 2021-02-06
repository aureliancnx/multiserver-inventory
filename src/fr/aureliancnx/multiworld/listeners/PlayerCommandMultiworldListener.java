package fr.aureliancnx.multiworld.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import fr.aureliancnx.multiworld.Multiworld;
import org.bukkit.Bukkit;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;

public class PlayerCommandMultiworldListener implements Listener
{
    public static Map<UUID, Long> dontTouch;
    
    static {
        PlayerCommandMultiworldListener.dontTouch = new HashMap<UUID, Long>();
    }
    
    @EventHandler
    public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
        if (event.getMessage() == null) {
            return;
        }
        if (!event.getMessage().startsWith("/multiworld ")) {
            return;
        }
        event.setCancelled(true);
        Logs.log("[AsgardiaMultiworld] Teleporting " + event.getPlayer() + " to " + event.getMessage().replace("/multiworld ", "") + "...", true);
        event.getPlayer().sendMessage("§aT\u00e9l\u00e9portation...");
        PlayerCommandMultiworldListener.dontTouch.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + 5000L);
        PlayerDisconnectListener.sendData(event.getPlayer());
        Bukkit.getScheduler().runTaskLater((Plugin)Multiworld.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                Logs.log("[AsgardiaMultiworld] " + event.getPlayer() + " sent to " + event.getMessage().replace("/multiworld ", "") + "...", true);
                PlayerCommandMultiworldListener.this.sendToServer(event.getPlayer(), event.getMessage().replace("/multiworld ", ""));
            }
        }, 20L);
    }
    
    private void sendToServer(final Player player, final String server) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage((Plugin)Multiworld.instance, "BungeeCord", out.toByteArray());
    }
}
