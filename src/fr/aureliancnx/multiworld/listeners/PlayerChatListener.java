package fr.aureliancnx.multiworld.listeners;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import fr.aureliancnx.multiworld.rabbit.RabbitSender;
import fr.aureliancnx.multiworld.rabbit.packets.PlayerChat;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import fr.aureliancnx.multiworld.Multiworld;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener
{
    private String queueName;
    private int chatExpire;
    
    public PlayerChatListener(final String queueName) {
        this.queueName = queueName;
        this.chatExpire = Multiworld.instance.getConfig().getInt("expiry.chat", 10000);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        event.setCancelled(true);
        final long expire = System.currentTimeMillis() + this.chatExpire;
        final String name = (player.getDisplayName() != null) ? player.getDisplayName() : player.getName();
        final String finalMessage = "§7" + ChatColor.translateAlternateColorCodes('&', Multiworld.chat.getPlayerPrefix(player)) + ChatColor.translateAlternateColorCodes('&', name) + ChatColor.translateAlternateColorCodes('&', Multiworld.chat.getPlayerSuffix(player)) + " §e§l\u21e8§f " + event.getMessage();
        final PlayerChat chat = new PlayerChat(player.getName(), event.getFormat(), finalMessage, expire);
        final String rawChat = chat.toJson();
        try {
            RabbitSender.publishMessage(this.queueName, rawChat, -1L);
        }
        catch (Exception e) {
            StackTraceElement[] stackTrace;
            for (int length = (stackTrace = e.getStackTrace()).length, i = 0; i < length; ++i) {
                final StackTraceElement t = stackTrace[i];
                Logs.log("[AsgardiaMultiworld] [WARN] " + t.toString(), true);
            }
            player.sendMessage("§cUne erreur est survenue lors de la publication de votre message.");
            player.sendMessage("§cVeuillez contacter un administrateur.");
        }
    }
}
