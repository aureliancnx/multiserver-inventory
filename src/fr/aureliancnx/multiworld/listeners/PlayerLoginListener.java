package fr.aureliancnx.multiworld.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import java.util.Iterator;
import fr.aureliancnx.multiworld.rabbit.utils.FileUtils;
import eisenwave.nbt.io.NBTSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import eisenwave.nbt.io.NBTDeserializer;
import eisenwave.nbt.NBTNamedTag;
import eisenwave.nbt.NBTByte;
import eisenwave.nbt.NBTType;
import eisenwave.nbt.NBTTag;
import eisenwave.nbt.NBTCompound;
import eisenwave.nbt.io.MojangsonParser;
import fr.aureliancnx.multiworld.rabbit.packets.TempPlayerData;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import fr.aureliancnx.multiworld.rabbit.listeners.ReceiveInventoryDataListener;
import java.io.File;
import fr.aureliancnx.multiworld.Multiworld;
import org.bukkit.event.player.PlayerPreLoginEvent;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;

public class PlayerLoginListener implements Listener
{
    public static long pp;
    public static Map<UUID, Long> newJoiners;
    
    static {
        PlayerLoginListener.pp = 0L;
        PlayerLoginListener.newJoiners = new HashMap<UUID, Long>();
    }
    
    @EventHandler
    public void onLogin(final PlayerPreLoginEvent event) {
        final UUID uuid = event.getUniqueId();
        long time = PlayerLoginListener.pp = System.currentTimeMillis();
        final String filePath = String.valueOf(Multiworld.instance.worldData) + "/playerdata/" + uuid + ".dat";
        File file = new File(filePath);
        if (!file.exists()) {
            PlayerLoginListener.newJoiners.put(uuid, System.currentTimeMillis() + 5000L);
        }
        if (ReceiveInventoryDataListener.tmpPlayerData.containsKey(uuid)) {
            try {
                Logs.log("[AsgardiaMultiworld] [" + event.getName() + "] Found player data", true);
                final TempPlayerData playerData = ReceiveInventoryDataListener.tmpPlayerData.get(uuid);
                if (playerData.isExpired()) {
                    if (Multiworld.instance.forcedInventoryDataReceive) {
                        Logs.log("[AsgardiaMultiworld] [WARN] [" + event.getName() + "] Expired player data but forced to receive inventory data.", true);
                        Logs.log("[AsgardiaMultiworld] [WARN] [" + event.getName() + "] Player kicked due to this. PLEASE SEE WHAT'S GOING ON!", true);
                        event.disallow(PlayerPreLoginEvent.Result.KICK_OTHER, "§cImpossible de r\u00e9cup\u00e9rer vos donn\u00e9es d'inventaire \u00e0 jour.");
                        event.disallow(PlayerPreLoginEvent.Result.KICK_OTHER, "§cAfin d'\u00e9viter les probl\u00e8mes d'inventaire, veuillez r\u00e9essayer ou contacter un administrateur.");
                        return;
                    }
                    ReceiveInventoryDataListener.tmpPlayerData.remove(uuid);
                    final long plo = System.currentTimeMillis() - playerData.expire;
                    Logs.log("[AsgardiaMultiworld] [" + event.getName() + "] Player data expired since " + plo + " ms. These data will not be taken into account", true);
                }
                else {
                    final String mson = playerData.rawWorldFileData;
                    final NBTNamedTag tag = MojangsonParser.parse(mson);
                    final NBTCompound root = (NBTCompound)tag.getTag();
                    for (final NBTTag tg : root.getList("Inventory")) {
                        final NBTCompound compound = (NBTCompound)tg;
                        if (compound.hasKeyOfType("Slot", NBTType.STRING) && compound.getString("Slot").equals("150b")) {
                            compound.put("Slot", new NBTByte((byte)(-106)));
                        }
                    }
                    NBTCompound rootBase = null;
                    if (file.exists()) {
                        final NBTNamedTag namedTagBase = new NBTDeserializer().fromFile(file);
                        rootBase = (NBTCompound)namedTagBase.getTag();
                    }
                    if (file.exists() && rootBase.hasKey("Pos")) {
                        root.put("Pos", rootBase.getTag("Pos"));
                    }
                    else {
                        final NBTCompound pos = new NBTCompound();
                        final Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
                        pos.putDouble("0", spawn.getX());
                        pos.putDouble("1", spawn.getY());
                        pos.putDouble("2", spawn.getZ());
                        root.put("Pos", pos);
                    }
                    if (file.exists() && rootBase.hasKey("Rotation")) {
                        root.put("Rotation", rootBase.getTag("Rotation"));
                    }
                    else {
                        final NBTCompound pos = new NBTCompound();
                        final Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
                        pos.putDouble("0", spawn.getPitch());
                        pos.putDouble("1", spawn.getYaw());
                        root.put("Pos", pos);
                    }
                    if (Multiworld.instance.debug) {
                        Logs.log("[AsgardiaMultiworld - DEBUG] [" + event.getName() + "] Player data are now: " + root.toMSONString(), false);
                    }
                    String path = String.valueOf(Multiworld.instance.worldData) + "/playerdata/" + uuid + ".dat";
                    file = new File(path);
                    new NBTSerializer().toFile(tag, file);
                    path = String.valueOf(Multiworld.instance.worldData) + "/playerdata/save" + uuid + ".dat";
                    file = new File(path);
                    new NBTSerializer().toFile(tag, file);
                    for (final Map.Entry<String, String> entry : playerData.dataToCopyPaste.entrySet()) {
                        final File f = new File(entry.getKey());
                        FileUtils.writeFile(f, entry.getValue());
                        Logs.log("[AsgardiaMultiworld] [" + event.getName() + "] Player data written: " + entry.getKey(), true);
                    }
                    ReceiveInventoryDataListener.tmpPlayerData.remove(uuid);
                    time = System.currentTimeMillis() - time;
                    Logs.log("[AsgardiaMultiworld] [" + event.getName() + "] Player data loaded in " + time + " ms.", true);
                }
            }
            catch (Exception err) {
                Logs.log("[AsgardiaMultiworld] [WARN] [" + event.getName() + "] /!\\ ----------- ERROR WHILE GETTING BACK PLAYER DATA ---------- /!\\", true);
                Logs.log("[AsgardiaMultiworld] [WARN] [" + event.getName() + "] /!\\ ----------- THIS CAN CORRUPT INVENTORY SYNC ---------- /!\\", true);
                Logs.log("[AsgardiaMultiworld] [WARN] [" + event.getName() + "] /!\\ ----------- STACK TRACE BELOW ---------- /!\\", true);
                err.printStackTrace();
                StackTraceElement[] stackTrace;
                for (int length = (stackTrace = err.getStackTrace()).length, i = 0; i < length; ++i) {
                    final StackTraceElement t = stackTrace[i];
                    Logs.log("[AsgardiaMultiworld] [WARN] " + t.toString(), false);
                }
                Logs.log("[AsgardiaMultiworld] [WARN] [" + event.getName() + "] /!\\ ----------- END ---------- /!\\", true);
            }
        }
        else {
            if (Multiworld.instance.forcedInventoryDataReceive) {
                Logs.log("[AsgardiaMultiworld] [WARN] [" + event.getName() + "] No player data found but forced to receive inventory data.", true);
                Logs.log("[AsgardiaMultiworld] [WARN] [" + event.getName() + "] Player kicked due to this. PLEASE SEE WHAT'S GOING ON!", true);
                event.disallow(PlayerPreLoginEvent.Result.KICK_OTHER, "§cImpossible de r\u00e9cup\u00e9rer vos donn\u00e9es d'inventaire.");
                event.disallow(PlayerPreLoginEvent.Result.KICK_OTHER, "§cAfin d'\u00e9viter les probl\u00e8mes d'inventaire, veuillez r\u00e9essayer ou contacter un administrateur.");
                return;
            }
            Logs.log("[AsgardiaMultiworld] [" + event.getName() + "] No player data found.", true);
        }
    }
}
