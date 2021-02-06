package fr.aureliancnx.multiworld.rabbit.tasks;

import fr.aureliancnx.multiworld.rabbit.packets.PlayerCommand;
import fr.aureliancnx.multiworld.rabbit.packets.TempPlayerData;
import java.util.List;
import fr.aureliancnx.multiworld.rabbit.listeners.ReceiveCommandListener;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import fr.aureliancnx.multiworld.rabbit.listeners.ReceiveInventoryDataListener;
import java.util.UUID;
import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import fr.aureliancnx.multiworld.Multiworld;

public class DustRemoverTask implements Runnable
{
    public DustRemoverTask(final Multiworld instance) {
        Bukkit.getScheduler().runTaskTimer((Plugin)instance, (Runnable)this, 6000L, 6000L);
    }
    
    @Override
    public void run() {
        final List<UUID> playerDataDust = new ArrayList<UUID>();
        ReceiveInventoryDataListener.tmpPlayerData.values().stream().filter(data -> data.isExpired()).forEach(data -> playerDataDust.add(data.uuid));
        playerDataDust.forEach(uuid -> ReceiveInventoryDataListener.tmpPlayerData.remove(uuid));
        Logs.log("[AsgardiaMultiworld] Removed " + playerDataDust.size() + " expired player data from memory.", true);
        final List<String> commandDataDust = new ArrayList<String>();
        ReceiveCommandListener.tmpCommandData.values().stream().filter(data -> data.isExpired()).forEach(data -> commandDataDust.add(data.playerName));
        commandDataDust.forEach(playerName -> ReceiveCommandListener.tmpCommandData.remove(playerName));
        Logs.log("[AsgardiaMultiworld] Removed " + commandDataDust.size() + " expired command orders from memory.", true);
    }
}
