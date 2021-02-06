package fr.aureliancnx.multiworld;

import org.bukkit.plugin.RegisteredServiceProvider;
import fr.aureliancnx.multiworld.rabbit.tasks.DustRemoverTask;
import fr.aureliancnx.multiworld.listeners.PlayerJoinListener;
import fr.aureliancnx.multiworld.listeners.PlayerCommandMultiworldListener;
import fr.aureliancnx.multiworld.listeners.PlayerCommandListener;
import fr.aureliancnx.multiworld.listeners.PlayerDropItemListener;
import fr.aureliancnx.multiworld.listeners.PlayerInventoryInteractListener;
import fr.aureliancnx.multiworld.listeners.PlayerChatListener;
import fr.aureliancnx.multiworld.listeners.PlayerLoginListener;
import org.bukkit.event.Listener;
import fr.aureliancnx.multiworld.listeners.PlayerDisconnectListener;
import org.bukkit.plugin.Plugin;
import fr.aureliancnx.multiworld.rabbit.listeners.ReceiveChatListener;
import fr.aureliancnx.multiworld.rabbit.listeners.ReceiveInventoryDataListener;
import fr.aureliancnx.multiworld.rabbit.RabbitConnection;
import fr.aureliancnx.multiworld.rabbit.utils.Logs;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.java.JavaPlugin;

public class Multiworld extends JavaPlugin
{
    public static Multiworld instance;
    public static Chat chat;
    public boolean disabled;
    public boolean debug;
    public boolean forcedInventoryDataReceive;
    public String worldData;
    public List<String> dataToCopyPaste;
    public List<Thread> workerThreads;
    
    static {
        Multiworld.chat = null;
    }
    
    public void onEnable() {
        (Multiworld.instance = this).reloadConfig();
        this.workerThreads = new ArrayList<Thread>();
        Logs.name = Clock.systemUTC().instant().toString();
        Logs.log("[AsgardiaMultiworld] Started logging to file: " + Logs.name, true);
        this.setupChat();
        if (!RabbitConnection.start(this.getConfig())) {
            return;
        }
        this.debug = this.getConfig().getBoolean("debug");
        this.worldData = this.getConfig().getString("worldName");
        this.forcedInventoryDataReceive = this.getConfig().getBoolean("forcedInventoryDataReceive", false);
        final String queueName = this.getConfig().getString("queueNames.receiveInventoryData");
        new ReceiveInventoryDataListener(queueName);
        final String sendInventoryQueue = this.getConfig().getString("queueNames.sendInventoryData");
        final String receiveChatQueueName = this.getConfig().getString("queueNames.receiveChatData");
        new ReceiveChatListener(receiveChatQueueName);
        final String chatQueueName = this.getConfig().getString("queueNames.sendChatData");
        this.dataToCopyPaste = (List<String>)this.getConfig().getStringList("dataToCopyPaste");
        this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerDisconnectListener(sendInventoryQueue), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerLoginListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerChatListener(chatQueueName), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerInventoryInteractListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerDropItemListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerCommandListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerCommandMultiworldListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerJoinListener(), (Plugin)this);
        new DustRemoverTask(this);
        Logs.log("[AsgardiaMultiworld] ------ READY ------", true);
    }
    
    private boolean setupChat() {
        final RegisteredServiceProvider<Chat> rsp = (RegisteredServiceProvider<Chat>)this.getServer().getServicesManager().getRegistration((Class)Chat.class);
        Multiworld.chat = (Chat)rsp.getProvider();
        return Multiworld.chat != null;
    }
    
    public void onDisable() {
        this.disabled = true;
        if (this.workerThreads != null) {
            this.workerThreads.forEach(thread -> thread.interrupt());
            this.workerThreads.forEach(thread -> thread.stop());
            Logs.log("[AsgardiaMultiworld] Disabled " + this.workerThreads.size() + " worker threads", true);
        }
    }
    
    public void insertThread(final Thread thread) {
        this.workerThreads.add(thread);
    }
}
