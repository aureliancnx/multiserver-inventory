// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class NBTMatcher
{
    private static final int EXACT = 1;
    private static final int TYPES_ONLY = 2;
    private final NBTTag pattern;
    private final int flags;
    
    public NBTMatcher(final NBTTag pattern, final int flags) {
        this.pattern = Objects.requireNonNull(pattern);
        this.flags = flags;
    }
    
    public boolean matches(final NBTTag tag) {
        return this.pattern.equals(tag);
    }
    
    private static class NBTMatcherList extends NBTTag
    {
        private final int hash;
        private final byte type;
        private final boolean empty;
        private final NBTTag[] value;
        
        public NBTMatcherList(final NBTList tag) {
            this.empty = tag.isEmpty();
            this.hash = tag.hashCode();
            this.type = tag.getTypeId();
            this.value = tag.getValue().toArray(new NBTTag[tag.size()]);
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof NBTList && this.equals((NBTList)obj);
        }
        
        public boolean equals(final NBTList tag) {
            return (this.empty && tag.isEmpty()) || (this.type == tag.getTypeId() && this.hash == tag.hashCode() && this.equals(tag.getValue()));
        }
        
        public boolean equals(final List<NBTTag> tags) {
            int index = 0;
            for (final NBTTag tag : tags) {
                if (!tag.equals(this.value[index++])) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public Object getValue() {
            return this.value;
        }
        
        @Override
        public NBTType getType() {
            return NBTType.LIST;
        }
        
        @Override
        public String toMSONString() {
            throw new UnsupportedOperationException();
        }
    }
}
