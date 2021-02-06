package fr.aureliancnx.multiworld.rabbit.packets;

import fr.aureliancnx.multiworld.rabbit.utils.GsonUtils;

public class PlayerChat
{
    public String playerName;
    public String format;
    public String message;
    public long expire;
    
    public PlayerChat(final String playerName, final String format, final String message, final long expire) {
        this.playerName = playerName;
        this.format = format;
        this.message = message;
        this.expire = expire;
    }
    
    public String toJson() {
        return GsonUtils.gson.toJson((Object)this);
    }
    
    public boolean isExpired() {
        return this.expire < System.currentTimeMillis();
    }
    
    public static PlayerChat fromJson(final String json) {
        return (PlayerChat)GsonUtils.gson.fromJson(json, (Class)PlayerChat.class);
    }
}
