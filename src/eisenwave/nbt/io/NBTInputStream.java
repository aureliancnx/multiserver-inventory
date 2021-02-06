// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt.io;

import eisenwave.nbt.NBTLongArray;
import eisenwave.nbt.NBTIntArray;
import java.util.Map;
import java.util.HashMap;
import eisenwave.nbt.NBTCompound;
import java.util.List;
import java.util.ArrayList;
import eisenwave.nbt.NBTList;
import eisenwave.nbt.NBTString;
import eisenwave.nbt.NBTByteArray;
import eisenwave.nbt.NBTEnd;
import eisenwave.nbt.NBTDouble;
import eisenwave.nbt.NBTFloat;
import eisenwave.nbt.NBTLong;
import eisenwave.nbt.NBTInt;
import eisenwave.nbt.NBTShort;
import eisenwave.nbt.NBTByte;
import eisenwave.nbt.NBTTag;
import eisenwave.nbt.NBTType;
import java.io.IOException;
import eisenwave.nbt.NBTNamedTag;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.io.DataInputStream;

public final class NBTInputStream extends DataInputStream
{
    private static final Charset UTF_8;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
    }
    
    public NBTInputStream(final InputStream in) {
        super(in);
    }
    
    public NBTNamedTag readNamedTag() throws IOException {
        return this.readNamedTag(0);
    }
    
    public NBTNamedTag readNamedTag(final int depth) throws IOException {
        final int id = this.read();
        if (id == -1) {
            return null;
        }
        final NBTType type = NBTType.getById((byte)id);
        final String name = (type != NBTType.END) ? this.readString() : "";
        return new NBTNamedTag(name, this.readTag(type, depth));
    }
    
    public NBTTag readTag(final NBTType type, final int depth) throws IOException {
        switch (type) {
            case END: {
                return this.readTagEnd(depth);
            }
            case BYTE: {
                return new NBTByte(this.readByte());
            }
            case SHORT: {
                return new NBTShort(this.readShort());
            }
            case INT: {
                return new NBTInt(this.readInt());
            }
            case LONG: {
                return new NBTLong(this.readLong());
            }
            case FLOAT: {
                return new NBTFloat(this.readFloat());
            }
            case DOUBLE: {
                return new NBTDouble(this.readDouble());
            }
            case BYTE_ARRAY: {
                return this.readTagByteArray();
            }
            case STRING: {
                return this.readTagString();
            }
            case LIST: {
                return this.readTagList(depth);
            }
            case COMPOUND: {
                return this.readTagCompound(depth);
            }
            case INT_ARRAY: {
                return this.readTagIntArray();
            }
            case LONG_ARRAY: {
                return this.readTagLongArray();
            }
            default: {
                throw new IOException("invalid tag type: " + type);
            }
        }
    }
    
    public NBTEnd readTagEnd(final int depth) throws IOException {
        if (depth == 0) {
            throw new IOException("TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
        }
        return NBTEnd.INSTANCE;
    }
    
    public NBTByteArray readTagByteArray() throws IOException {
        final int length = this.readInt();
        final byte[] bytes = new byte[length];
        this.readFully(bytes);
        return new NBTByteArray(bytes);
    }
    
    public NBTString readTagString() throws IOException {
        return new NBTString(this.readString());
    }
    
    public NBTList readTagList(final int depth) throws IOException {
        final NBTType elementType = NBTType.getById(this.readByte());
        final int length = this.readInt();
        if (elementType == NBTType.END && length > 0) {
            throw new IOException("List is of type TAG_End but not empty");
        }
        final List<NBTTag> tagList = new ArrayList<NBTTag>();
        for (int i = 0; i < length; ++i) {
            final NBTTag tag = this.readTag(elementType, depth + 1);
            tagList.add(tag);
        }
        return new NBTList(elementType, tagList);
    }
    
    public NBTCompound readTagCompound(final int depth) throws IOException {
        final Map<String, NBTTag> tagMap = new HashMap<String, NBTTag>();
        while (true) {
            final NBTNamedTag namedTag = this.readNamedTag(depth + 1);
            if (namedTag == null) {
                throw new IOException("NBT ends inside a list");
            }
            final NBTTag tag = namedTag.getTag();
            if (tag instanceof NBTEnd) {
                return new NBTCompound(tagMap);
            }
            tagMap.put(namedTag.getName(), tag);
        }
    }
    
    public NBTIntArray readTagIntArray() throws IOException {
        final int length = this.readInt();
        final int[] data = new int[length];
        for (int i = 0; i < length; ++i) {
            data[i] = this.readInt();
        }
        return new NBTIntArray(data);
    }
    
    public NBTLongArray readTagLongArray() throws IOException {
        final int length = this.readInt();
        final long[] data = new long[length];
        for (int i = 0; i < length; ++i) {
            data[i] = this.readLong();
        }
        return new NBTLongArray(data);
    }
    
    public String readString() throws IOException {
        final int length = this.readUnsignedShort();
        final byte[] bytes = new byte[length];
        this.readFully(bytes);
        return new String(bytes, NBTInputStream.UTF_8);
    }
}
