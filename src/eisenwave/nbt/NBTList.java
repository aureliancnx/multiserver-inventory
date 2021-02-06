// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;

public final class NBTList extends NBTTag implements Iterable<NBTTag>, Cloneable
{
    private NBTType type;
    private final List<NBTTag> list;
    
    public NBTList(final NBTType type, final List<? extends NBTTag> value) {
        this.list = new ArrayList<NBTTag>();
        this.type = type;
        value.forEach(this::add);
    }
    
    public NBTList(final NBTType type, final NBTTag... value) {
        this(type, Arrays.asList(value));
    }
    
    public NBTList(final NBTType type) {
        this.list = new ArrayList<NBTTag>();
        this.type = type;
    }
    
    public NBTList() {
        this(null);
    }
    
    public int size() {
        return this.list.size();
    }
    
    @Override
    public List<NBTTag> getValue() {
        return this.list;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.LIST;
    }
    
    public NBTType getElementType() {
        if (this.type == null) {
            return NBTType.COMPOUND;
        }
        return this.type;
    }
    
    public NBTTag get(final int index) {
        return this.list.get(index);
    }
    
    public boolean isEmpty() {
        return this.list.isEmpty();
    }
    
    public void add(final NBTTag value) {
        if (this.type == null) {
            this.type = value.getType();
        }
        else if (this.type != value.getType()) {
            throw new IllegalArgumentException(value.getType() + " is not of expected type " + this.type);
        }
        this.list.add(value);
    }
    
    public void add(final int index, final NBTTag value) {
        if (index < 0 || index >= this.list.size()) {
            throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        if (this.type == null) {
            this.type = value.getType();
        }
        else if (this.type != value.getType()) {
            throw new IllegalArgumentException(value.getType() + " is not of expected type " + this.type);
        }
        this.list.add(index, value);
    }
    
    public void addAll(final Collection<? extends NBTTag> values) {
        values.forEach(this::add);
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTList && this.equals((NBTList)obj);
    }
    
    public boolean equals(final NBTList tag) {
        return (this.isEmpty() && tag.isEmpty()) || (this.type == tag.type && this.list.equals(tag.list));
    }
    
    @Override
    public Iterator<NBTTag> iterator() {
        return this.list.iterator();
    }
    
    @Override
    public String toMSONString() {
        final StringBuilder builder = new StringBuilder("[");
        final Iterator<NBTTag> iter = this.iterator();
        boolean first = true;
        while (iter.hasNext()) {
            if (first) {
                first = false;
            }
            else {
                builder.append(',');
            }
            builder.append(iter.next().toMSONString());
        }
        return builder.append("]").toString();
    }
    
    public NBTList clone() {
        return new NBTList(this.type, this.list);
    }
}
