// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.Objects;
import java.util.Collections;
import java.util.Set;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class NBTCompound extends NBTTag
{
    private static final Pattern SIMPLE_STRING;
    private final Map<String, NBTTag> value;
    
    static {
        SIMPLE_STRING = Pattern.compile("[A-Za-z0-9._+-]+");
    }
    
    public NBTCompound(final Map<String, NBTTag> value) {
        this.value = new LinkedHashMap<String, NBTTag>(value);
    }
    
    public NBTCompound(final NBTNamedTag... tags) {
        this.value = new LinkedHashMap<String, NBTTag>();
        for (final NBTNamedTag tag : tags) {
            this.value.put(tag.getName(), tag.getTag());
        }
    }
    
    public NBTCompound() {
        this.value = new LinkedHashMap<String, NBTTag>();
    }
    
    public int size() {
        return this.value.size();
    }
    
    @Override
    public Map<String, NBTTag> getValue() {
        return this.value;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.COMPOUND;
    }
    
    public NBTTag getTag(final String key) {
        if (!this.hasKey(key)) {
            throw new NoSuchElementException(key);
        }
        return this.value.get(key);
    }
    
    public byte getByte(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTByte)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTByte)tag).getValue();
    }
    
    public short getShort(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTShort)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTShort)tag).getValue();
    }
    
    public int getInt(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTInt)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTInt)tag).getValue();
    }
    
    public long getLong(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTLong)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTLong)tag).getValue();
    }
    
    public float getFloat(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTFloat)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTFloat)tag).getValue();
    }
    
    public double getDouble(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTDouble)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTDouble)tag).getValue();
    }
    
    public byte[] getByteArray(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTByteArray)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTByteArray)tag).getValue();
    }
    
    public String getString(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTString)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTString)tag).getValue();
    }
    
    public List<NBTTag> getList(final String key) {
        return this.getTagList(key).getValue();
    }
    
    public NBTList getTagList(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTList)) {
            throw new NoSuchElementException(key);
        }
        return (NBTList)tag;
    }
    
    public Map<String, NBTTag> getCompound(final String key) {
        return this.getCompoundTag(key).getValue();
    }
    
    public NBTCompound getCompoundTag(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTCompound)) {
            throw new NoSuchElementException(key);
        }
        return (NBTCompound)tag;
    }
    
    public int[] getIntArray(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTIntArray)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTIntArray)tag).getValue();
    }
    
    public long[] getLongArray(final String key) {
        final NBTTag tag = this.value.get(key);
        if (!(tag instanceof NBTLongArray)) {
            throw new NoSuchElementException(key);
        }
        return ((NBTLongArray)tag).getValue();
    }
    
    public Set<String> getKeys() {
        return Collections.unmodifiableSet((Set<? extends String>)this.value.keySet());
    }
    
    public boolean isEmpty() {
        return this.value.isEmpty();
    }
    
    public boolean hasKey(final String key) {
        return this.value.containsKey(key);
    }
    
    public boolean hasKeyOfType(final String key, final NBTType type) {
        Objects.requireNonNull(type);
        return this.value.containsKey(key) && this.value.get(key).getType() == type;
    }
    
    public void put(final String name, final NBTTag tag) {
        this.value.put(name, tag);
    }
    
    public void putByteArray(final String key, final byte[] value) {
        this.put(key, new NBTByteArray(value));
    }
    
    public void putByte(final String key, final byte value) {
        this.put(key, new NBTByte(value));
    }
    
    public void putDouble(final String key, final double value) {
        this.put(key, new NBTDouble(value));
    }
    
    public void putFloat(final String key, final float value) {
        this.put(key, new NBTFloat(value));
    }
    
    public void putIntArray(final String key, final int[] value) {
        this.put(key, new NBTIntArray(value));
    }
    
    public void putLongArray(final String key, final long[] value) {
        this.put(key, new NBTLongArray(value));
    }
    
    public void putInt(final String key, final int value) {
        this.put(key, new NBTInt(value));
    }
    
    public void putLong(final String key, final long value) {
        this.put(key, new NBTLong(value));
    }
    
    public void putShort(final String key, final short value) {
        this.put(key, new NBTShort(value));
    }
    
    public void putString(final String key, final String value) {
        this.put(key, new NBTString(value));
    }
    
    public void forEach(final BiConsumer<String, ? super NBTTag> action) {
        this.value.forEach(action::accept);
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTCompound && this.equals((NBTCompound)obj);
    }
    
    public boolean equals(final NBTCompound tag) {
        return (this.isEmpty() && tag.isEmpty()) || this.value.equals(tag.value);
    }
    
    @Override
    public String toMSONString() {
        final StringBuilder builder = new StringBuilder("{");
        final Set<String> keys = this.value.keySet();
        for (final String key : keys) {
            if (builder.length() > 1) {
                builder.append(',');
            }
            builder.append(NBTCompound.SIMPLE_STRING.matcher(key).matches() ? key : NBTString.toMSONString(key)).append(':').append(this.value.get(key).toMSONString());
        }
        return builder.append("}").toString();
    }
}
