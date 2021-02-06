// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

public final class NBTShort extends NBTTag implements Cloneable
{
    private short value;
    
    public NBTShort(final short value) {
        this.value = value;
    }
    
    @Override
    public Short getValue() {
        return this.value;
    }
    
    public short getShortValue() {
        return this.value;
    }
    
    public void setShortValue(final short value) {
        this.value = value;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.SHORT;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTShort && this.equals((NBTShort)obj);
    }
    
    public boolean equals(final NBTShort tag) {
        return this.value == tag.value;
    }
    
    @Override
    public int hashCode() {
        return Short.hashCode(this.value);
    }
    
    @Override
    public String toMSONString() {
        return String.valueOf(this.value) + "s";
    }
    
    public NBTShort clone() {
        return new NBTShort(this.value);
    }
}
