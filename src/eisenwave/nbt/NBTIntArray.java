// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

import java.util.Arrays;
import java.util.Objects;

public final class NBTIntArray extends NBTTag implements Cloneable
{
    private final int[] value;
    
    public NBTIntArray(final int[] value) {
        this.value = Objects.requireNonNull(value);
    }
    
    public NBTIntArray(final Number[] numbers) {
        this.value = new int[numbers.length];
        for (int i = 0; i < numbers.length; ++i) {
            this.value[i] = numbers[i].intValue();
        }
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public int[] getValue() {
        return this.value;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.INT_ARRAY;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTIntArray && this.equals((NBTIntArray)obj);
    }
    
    public boolean equals(final NBTIntArray tag) {
        return Arrays.equals(this.value, tag.value);
    }
    
    @Override
    public String toMSONString() {
        final StringBuilder stringbuilder = new StringBuilder("[I;");
        for (int i = 0; i < this.value.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(this.value[i]);
        }
        return stringbuilder.append(']').toString();
    }
    
    public NBTIntArray clone() {
        return new NBTIntArray(this.value);
    }
}
