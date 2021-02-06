package fr.aureliancnx.multiworld.rabbit.packets;

import fr.aureliancnx.multiworld.rabbit.utils.GsonUtils;

public class PlayerCommand
{
    public String playerName;
    public String command;
    public long expire;
    
    public PlayerCommand(final String playerName, final String command, final long expire) {
        this.playerName = playerName;
        this.command = command;
        this.expire = expire;
    }
    
    public String toJson() {
        return GsonUtils.gson.toJson((Object)this);
    }
    
    public boolean isExpired() {
        return this.expire < System.currentTimeMillis();
    }
    
    public static PlayerCommand fromJson(final String json) {
        return (PlayerCommand)GsonUtils.gson.fromJson(json, (Class)PlayerCommand.class);
    }
}
