// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

public final class NBTString extends NBTTag implements Cloneable
{
    private String value;
    
    public NBTString(final String value) {
        this.setValue(value);
    }
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public NBTType getType() {
        return NBTType.STRING;
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    
    @Override
    public String toMSONString() {
        return toMSONString(this.value);
    }
    
    public NBTString clone() {
        return new NBTString(this.value);
    }
    
    public static String toMSONString(final String str) {
        final StringBuilder builder = new StringBuilder("\"");
        final char[] chars = str.toCharArray();
        char[] array;
        for (int length = (array = chars).length, i = 0; i < length; ++i) {
            final char c = array[i];
            if (c == '\\' || c == '\"') {
                builder.append('\\');
            }
            builder.append(c);
        }
        return builder.append('\"').toString();
    }
}
