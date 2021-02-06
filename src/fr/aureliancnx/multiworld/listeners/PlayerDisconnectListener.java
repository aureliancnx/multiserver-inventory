package fr.aureliancnx.multiworld.listeners;

import java.util.Iterator;
import java.util.Map;
import fr.aureliancnx.multiworld.rabbit.RabbitSender;
import fr.aureliancnx.multiworld.rabbit.packets.TempPlayerData;
import fr.aureliancnx.multiworld.rabbit.utils.FileUtils;
import java.util.HashMap;
import eisenwave.nbt.NBTTag;
import eisenwave.nbt.NBTCompound;
import eisenwave.nbt.io.NBTDeserializer;
import eisenwave.nbt.NBTNamedTag;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import java.io.File;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import fr.aureliancnx.multiworld.Multiworld;
import org.bukkit.event.Listener;

public class PlayerDisconnectListener implements Listener
{
    static String queueName;
    static int playerDataExpiry;
    
    public PlayerDisconnectListener(final String qName) {
        PlayerDisconnectListener.queueName = qName;
        PlayerDisconnectListener.playerDataExpiry = Multiworld.instance.getConfig().getInt("expiry.playerData", 10000);
    }
    
    @EventHandler
    public void onDisconnect(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (PlayerCommandMultiworldListener.dontTouch.containsKey(player.getUniqueId()) && PlayerCommandMultiworldListener.dontTouch.get(player.getUniqueId()) > System.currentTimeMillis()) {
            return;
        }
        sendData(player);
    }
    
    public static void sendData(final Player player) {
        player.saveData();
        try {
            final String filePath = String.valueOf(Multiworld.instance.worldData) + "/playerdata/" + player.getUniqueId() + ".dat";
            final File file = new File(filePath);
            if (!file.exists()) {
                Logs.log("[AsgardiaMultiworld] [WARN] [" + player.getName() + "] /!\\\\ Player data file not found /!\\", true);
                Logs.log("[AsgardiaMultiworld] [WARN] [" + player.getName() + "] " + filePath, true);
                return;
            }
            final NBTNamedTag namedTag = new NBTDeserializer().fromFile(file);
            final NBTCompound root = (NBTCompound)namedTag.getTag();
            root.put("Pos", new NBTCompound());
            root.put("Rotation", new NBTCompound());
            final long expire = System.currentTimeMillis() + PlayerDisconnectListener.playerDataExpiry;
            final Map<String, String> dataToCopyPaste = new HashMap<String, String>();
            if (Multiworld.instance.dataToCopyPaste != null) {
                for (final String path : Multiworld.instance.dataToCopyPaste) {
                    String p = path.replace("<player>", player.getName());
                    p = p.replace("<uuid>", player.getUniqueId().toString());
                    final File f = new File(p);
                    if (!f.exists()) {
                        Logs.log("[AsgardiaMultiworld] [WARN] [" + player.getName() + "] Player data to copy paste not found: " + p, true);
                    }
                    else {
                        final String fileData = new String(FileUtils.readFile(f.getAbsolutePath()));
                        dataToCopyPaste.put(p, fileData);
                        Logs.log("[AsgardiaMultiworld] [" + player.getName() + "] Copied " + p + ": " + fileData.length() + " length", true);
                    }
                }
            }
            final TempPlayerData playerData = new TempPlayerData(player.getUniqueId(), expire, root.toMSONString(), dataToCopyPaste);
            RabbitSender.publishMessage(PlayerDisconnectListener.queueName, playerData.toJson(), -1L);
        }
        catch (Exception e) {
            Logs.log("[AsgardiaMultiworld] [WARN] [" + player.getName() + "] /!\\ ----------- ERROR WHILE SENDING PLAYER DATA ---------- /!\\", true);
            Logs.log("[AsgardiaMultiworld] [WARN] [" + player.getName() + "] /!\\ ----------- THIS CAN CORRUPT INVENTORY SYNC ---------- /!\\", true);
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
