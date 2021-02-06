// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

public class NBTNamedTag
{
    private final String name;
    private final NBTTag tag;
    
    public NBTNamedTag(final String name, final NBTTag tag) {
        this.name = name;
        this.tag = tag;
    }
    
    public String getName() {
        return this.name;
    }
    
    public NBTTag getTag() {
        return this.tag;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NBTNamedTag && this.equals((NBTNamedTag)obj);
    }
    
    public boolean equals(final NBTNamedTag tag) {
        return this.name.equals(tag.name) && this.tag.equals(tag.tag);
    }
}
