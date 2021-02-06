package fr.aureliancnx.multiworld.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import fr.aureliancnx.multiworld.rabbit.listeners.ReceiveCommandListener;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import java.util.HashMap;
import fr.aureliancnx.multiworld.Multiworld;
import fr.aureliancnx.multiworld.rabbit.utils.CommandAction;
import java.util.Map;
import org.bukkit.event.Listener;

public class PlayerCommandListener implements Listener
{
    private Map<String, CommandAction> commands;
    public static int commandExpiry;
    public static String senderQueue;
    
    public PlayerCommandListener(final Multiworld instance) {
        this.commands = new HashMap<String, CommandAction>();
        final ConfigurationSection section = instance.getConfig().getConfigurationSection("commands");
        if (section != null && section.getKeys(false) != null) {
            for (final String key : section.getKeys(false)) {
                final CommandAction commandAction = new CommandAction(section.getConfigurationSection(key));
                this.commands.put(key, commandAction);
                Logs.log("[AsgardiaMultiworld] Loaded command: " + key, true);
            }
        }
        PlayerCommandListener.commandExpiry = instance.getConfig().getInt("expiry.commandToAnotherServer", 2000);
        PlayerCommandListener.senderQueue = instance.getConfig().getString("queueNames.sendCommandData");
        final String receiveQueue = instance.getConfig().getString("queueNames.receiveCommandData");
        new ReceiveCommandListener(receiveQueue);
    }
    
    @EventHandler
    public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
        if (event.getMessage() == null) {
            return;
        }
        String command = event.getMessage().toLowerCase().trim();
        command = command.replace("/", "");
        if (!this.commands.containsKey(command)) {
            return;
        }
        event.setCancelled(true);
        final CommandAction action = this.commands.get(command);
        action.work(event.getPlayer());
    }
}
