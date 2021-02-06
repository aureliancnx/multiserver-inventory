// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

public final class NBTEnd extends NBTTag implements Cloneable
{
    public static final NBTEnd INSTANCE;
    
    static {
        INSTANCE = new NBTEnd();
    }
    
    private NBTEnd() {
    }
    
    @Override
    public Void getValue() {
        return null;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.END;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTEnd;
    }
    
    @Override
    public String toMSONString() {
        return "END";
    }
    
    public NBTEnd clone() {
        return new NBTEnd();
    }
}
