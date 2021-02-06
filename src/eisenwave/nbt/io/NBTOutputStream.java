// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt.io;

import java.util.Map;
import java.util.Iterator;
import java.util.List;
import eisenwave.nbt.NBTLongArray;
import eisenwave.nbt.NBTIntArray;
import eisenwave.nbt.NBTCompound;
import eisenwave.nbt.NBTList;
import eisenwave.nbt.NBTString;
import eisenwave.nbt.NBTByteArray;
import eisenwave.nbt.NBTDouble;
import eisenwave.nbt.NBTFloat;
import eisenwave.nbt.NBTLong;
import eisenwave.nbt.NBTInt;
import eisenwave.nbt.NBTShort;
import eisenwave.nbt.NBTByte;
import eisenwave.nbt.NBTNamedTag;
import java.io.IOException;
import java.util.Objects;
import eisenwave.nbt.NBTTag;
import java.io.OutputStream;
import eisenwave.nbt.NBTType;
import java.nio.charset.Charset;
import java.io.DataOutputStream;

public final class NBTOutputStream extends DataOutputStream
{
    private static final Charset UTF_8;
    private static final int END_ID;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
        END_ID = NBTType.END.getId();
    }
    
    public NBTOutputStream(final OutputStream out) {
        super(out);
    }
    
    public void writeNamedTag(final String name, final NBTTag tag) throws IOException {
        Objects.requireNonNull(tag);
        final int typeId = tag.getType().getId();
        final byte[] nameBytes = name.getBytes(NBTOutputStream.UTF_8);
        this.writeByte(typeId);
        this.writeShort(nameBytes.length);
        this.write(nameBytes);
        if (typeId == NBTOutputStream.END_ID) {
            throw new IOException("Named TAG_End not permitted.");
        }
        this.writeTag(tag);
    }
    
    public void writeNamedTag(final NBTNamedTag tag) throws IOException {
        this.writeNamedTag(tag.getName(), tag.getTag());
    }
    
    public void writeTag(final NBTTag tag) throws IOException {
        switch (tag.getType()) {
            case END: {
                break;
            }
            case BYTE: {
                this.writeByte(((NBTByte)tag).getByteValue());
                break;
            }
            case SHORT: {
                this.writeShort(((NBTShort)tag).getShortValue());
                break;
            }
            case INT: {
                this.writeInt(((NBTInt)tag).getIntValue());
                break;
            }
            case LONG: {
                this.writeLong(((NBTLong)tag).getLongValue());
                break;
            }
            case FLOAT: {
                this.writeFloat(((NBTFloat)tag).getFloatValue());
                break;
            }
            case DOUBLE: {
                this.writeDouble(((NBTDouble)tag).getDoubleValue());
                break;
            }
            case BYTE_ARRAY: {
                this.writeTagByteArray((NBTByteArray)tag);
                break;
            }
            case STRING: {
                this.writeTagString((NBTString)tag);
                break;
            }
            case LIST: {
                this.writeTagList((NBTList)tag);
                break;
            }
            case COMPOUND: {
                this.writeTagCompound((NBTCompound)tag);
                break;
            }
            case INT_ARRAY: {
                this.writeTagIntArray((NBTIntArray)tag);
                break;
            }
            case LONG_ARRAY: {
                this.writeTagLongArray((NBTLongArray)tag);
                break;
            }
            default: {
                throw new IOException("invalid tag type: " + tag.getType());
            }
        }
    }
    
    public void writeTagString(final NBTString tag) throws IOException {
        final byte[] bytes = tag.getValue().getBytes(NBTOutputStream.UTF_8);
        this.writeShort(bytes.length);
        this.write(bytes);
    }
    
    public void writeTagByteArray(final NBTByteArray tag) throws IOException {
        final byte[] bytes = tag.getValue();
        this.writeInt(bytes.length);
        this.write(bytes);
    }
    
    public void writeTagList(final NBTList tag) throws IOException {
        final NBTType type = tag.getElementType();
        final List<? extends NBTTag> tags = tag.getValue();
        final int size = tags.size();
        this.writeByte(type.getId());
        this.writeInt(size);
        for (final NBTTag element : tags) {
            this.writeTag(element);
        }
    }
    
    public void writeTagCompound(final NBTCompound tag) throws IOException {
        for (final Map.Entry<String, NBTTag> entry : tag.getValue().entrySet()) {
            this.writeNamedTag(entry.getKey(), entry.getValue());
        }
        this.writeByte(NBTOutputStream.END_ID);
    }
    
    public void writeTagIntArray(final NBTIntArray tag) throws IOException {
        this.writeInt(tag.length());
        int[] value;
        for (int length = (value = tag.getValue()).length, i = 0; i < length; ++i) {
            final int aData = value[i];
            this.writeInt(aData);
        }
    }
    
    public void writeTagLongArray(final NBTLongArray tag) throws IOException {
        this.writeInt(tag.length());
        long[] value;
        for (int length = (value = tag.getValue()).length, i = 0; i < length; ++i) {
            final long aData = value[i];
            this.writeLong(aData);
        }
    }
}
