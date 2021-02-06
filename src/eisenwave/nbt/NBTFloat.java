// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

public final class NBTFloat extends NBTTag implements Cloneable
{
    private float value;
    
    public NBTFloat(final float value) {
        this.value = value;
    }
    
    @Override
    public Float getValue() {
        return this.value;
    }
    
    public float getFloatValue() {
        return this.value;
    }
    
    public void setFloatValue(final float value) {
        this.value = value;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.FLOAT;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTFloat && this.equals((NBTFloat)obj);
    }
    
    public boolean equals(final NBTFloat tag) {
        return this.value == tag.value;
    }
    
    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }
    
    @Override
    public String toMSONString() {
        return String.valueOf(this.value) + "f";
    }
    
    public NBTFloat clone() {
        return new NBTFloat(this.value);
    }
}
