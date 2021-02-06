// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

public final class NBTByte extends NBTTag implements Cloneable
{
    private byte value;
    
    public NBTByte(final byte value) {
        this.value = value;
    }
    
    @Override
    public Byte getValue() {
        return this.value;
    }
    
    public byte getByteValue() {
        return this.value;
    }
    
    public void setByteValue(final byte value) {
        this.value = value;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.BYTE;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTByte && this.equals((NBTByte)obj);
    }
    
    public boolean equals(final NBTByte tag) {
        return this.value == tag.value;
    }
    
    @Override
    public int hashCode() {
        return Byte.hashCode(this.value);
    }
    
    @Override
    public String toMSONString() {
        return String.valueOf(Byte.toUnsignedInt(this.value)) + "b";
    }
    
    public NBTByte clone() {
        return new NBTByte(this.value);
    }
}
