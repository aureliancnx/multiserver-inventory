// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

public abstract class NBTTag
{
    public abstract Object getValue();
    
    public abstract NBTType getType();
    
    public byte getTypeId() {
        return this.getType().getId();
    }
    
    public abstract String toMSONString();
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof NBTTag) {
            final NBTTag tag = (NBTTag)obj;
            return this.getType() == tag.getType() && this.getValue().equals(tag.getValue());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }
    
    @Override
    public String toString() {
        return this.toMSONString();
    }
}
