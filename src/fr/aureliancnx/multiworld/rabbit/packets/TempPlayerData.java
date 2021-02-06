package fr.aureliancnx.multiworld.rabbit.packets;

import fr.aureliancnx.multiworld.rabbit.utils.GsonUtils;
import java.util.Map;
import java.util.UUID;

public class TempPlayerData
{
    public UUID uuid;
    public long expire;
    public String rawWorldFileData;
    public Map<String, String> dataToCopyPaste;
    
    public TempPlayerData(final UUID uuid, final long expire, final String rawWorldFileData, final Map<String, String> dataToCopyPaste) {
        this.uuid = uuid;
        this.expire = expire;
        this.rawWorldFileData = rawWorldFileData;
        this.dataToCopyPaste = dataToCopyPaste;
    }
    
    public String toJson() {
        return GsonUtils.gson.toJson((Object)this);
    }
    
    public boolean isExpired() {
        return this.expire < System.currentTimeMillis();
    }
    
    public static TempPlayerData fromJson(final String json) {
        return (TempPlayerData)GsonUtils.gson.fromJson(json, (Class)TempPlayerData.class);
    }
}
