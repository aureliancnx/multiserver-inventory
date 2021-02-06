// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

public final class NBTDouble extends NBTTag implements Cloneable
{
    private double value;
    
    public NBTDouble(final double value) {
        this.value = value;
    }
    
    @Override
    public Double getValue() {
        return this.value;
    }
    
    public double getDoubleValue() {
        return this.value;
    }
    
    public void setDoubleValue(final double value) {
        this.value = value;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.DOUBLE;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTDouble && this.equals((NBTDouble)obj);
    }
    
    public boolean equals(final NBTDouble tag) {
        return this.value == tag.value;
    }
    
    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }
    
    @Override
    public String toMSONString() {
        return String.valueOf(this.value) + "d";
    }
    
    public NBTDouble clone() {
        return new NBTDouble(this.value);
    }
}
