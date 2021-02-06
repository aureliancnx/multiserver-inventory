// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

import java.util.Arrays;

public final class NBTByteArray extends NBTTag
{
    private final byte[] value;
    
    public NBTByteArray(final byte[] value) {
        this.value = value;
    }
    
    public NBTByteArray(final Number[] numbers) {
        this.value = new byte[numbers.length];
        for (int i = 0; i < numbers.length; ++i) {
            this.value[i] = numbers[i].byteValue();
        }
    }
    
    public int length() {
        return this.value.length;
    }
    
    @Override
    public byte[] getValue() {
        return this.value;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.BYTE_ARRAY;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTByteArray && this.equals((NBTByteArray)obj);
    }
    
    public boolean equals(final NBTByteArray tag) {
        return Arrays.equals(this.value, tag.value);
    }
    
    @Override
    public String toMSONString() {
        final StringBuilder stringbuilder = new StringBuilder("[B;");
        for (int i = 0; i < this.value.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(this.value[i]).append('B');
        }
        return stringbuilder.append(']').toString();
    }
}
