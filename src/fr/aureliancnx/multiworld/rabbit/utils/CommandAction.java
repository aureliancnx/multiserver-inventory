// 
// Decompiled by Procyon v0.5.36
// 

package fr.aureliancnx.multiworld.rabbit.utils;

import com.google.common.io.ByteArrayDataOutput;
import org.bukkit.plugin.Plugin;
import com.google.common.io.ByteStreams;
import fr.aureliancnx.multiworld.Multiworld;
import org.bukkit.Bukkit;
import fr.aureliancnx.multiworld.listeners.PlayerDisconnectListener;
import fr.aureliancnx.multiworld.listeners.PlayerCommandMultiworldListener;
import fr.aureliancnx.multiworld.rabbit.RabbitSender;
import fr.aureliancnx.multiworld.rabbit.packets.PlayerCommand;
import fr.aureliancnx.multiworld.listeners.PlayerCommandListener;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

public class CommandAction
{
    public CommandActionType type;
    public String data;
    
    public CommandAction(final CommandActionType type, final String data) {
        this.type = type;
        this.data = data;
    }
    
    public CommandAction(final ConfigurationSection section) {
        this.type = CommandActionType.get(section.getString("type"));
        this.data = section.getString("data");
    }
    
    public void work(final Player player) {
        if (this.type.equals(CommandActionType.CHANGE_SERVER)) {
            this.sendToServer(player, this.data);
            return;
        }
        if (this.type.equals(CommandActionType.CHANGE_SERVER_EXECUTE_COMMAND)) {
            final String[] args = this.data.split(",");
            this.sendToServer(player, args[0]);
            if (args.length > 1) {
                final String command = args[1];
                final PlayerCommand cmd = new PlayerCommand(player.getName(), command, System.currentTimeMillis() + PlayerCommandListener.commandExpiry);
                try {
                    RabbitSender.publishMessage(PlayerCommandListener.senderQueue, cmd.toJson(), -1L);
                }
                catch (Exception e) {
                    Logs.log("[AsgardiaMultiworld] [WARN] [" + player.getName() + "] /!\\ ----------- ERROR WHILE SENDING COMMANND ORDER ---------- /!\\", true);
                    Logs.log("[AsgardiaMultiworld] [WARN] [" + player.getName() + "] /!\\ ----------- COMMAND: " + command + " ---------- /!\\", true);
                    Logs.log("[AsgardiaMultiworld] [WARN] [" + player.getName() + "] /!\\ ----------- STACK TRACE BELOW ---------- /!\\", true);
                    StackTraceElement[] stackTrace;
                    for (int length = (stackTrace = e.getStackTrace()).length, i = 0; i < length; ++i) {
                        final StackTraceElement t = stackTrace[i];
                        Logs.log("[AsgardiaMultiworld] [WARN] " + t.toString(), true);
                    }
                    Logs.log("[AsgardiaMultiworld] [WARN] [" + player.getName() + "] /!\\ ----------- END ---------- /!\\", true);
                }
            }
        }
    }
    
    private void sendToServer(final Player player, final String server) {
        player.sendMessage("§aT\u00e9l\u00e9portation...");
        PlayerCommandMultiworldListener.dontTouch.put(player.getUniqueId(), System.currentTimeMillis() + 5000L);
        PlayerDisconnectListener.sendData(player);
        Bukkit.getScheduler().runTaskLater((Plugin)Multiworld.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(server);
                player.sendPluginMessage((Plugin)Multiworld.instance, "BungeeCord", out.toByteArray());
            }
        }, 20L);
    }
    
    public enum CommandActionType
    {
        CHANGE_SERVER("CHANGE_SERVER", 0), 
        CHANGE_SERVER_EXECUTE_COMMAND("CHANGE_SERVER_EXECUTE_COMMAND", 1);
        
        private CommandActionType(final String name, final int ordinal) {
        }
        
        public static CommandActionType get(final String raw) {
            CommandActionType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final CommandActionType type = values[i];
                if (type.name().equalsIgnoreCase(raw)) {
                    return type;
                }
            }
            return CommandActionType.CHANGE_SERVER;
        }
    }
}
