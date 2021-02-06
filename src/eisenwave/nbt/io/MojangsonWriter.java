// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt.io;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import eisenwave.nbt.NBTList;
import eisenwave.nbt.NBTCompound;
import eisenwave.nbt.NBTType;
import eisenwave.nbt.NBTNamedTag;
import java.io.IOException;
import eisenwave.nbt.NBTString;
import eisenwave.nbt.NBTTag;
import java.util.Objects;
import java.util.regex.Pattern;
import java.io.Writer;

public class MojangsonWriter extends Writer
{
    private static final String NEWLINE;
    private static final String INDENT = "    ";
    private static final Pattern SIMPLE_STRING;
    private final Writer writer;
    private final boolean pretty;
    private int indent;
    
    static {
        NEWLINE = System.getProperty("line.separator");
        SIMPLE_STRING = Pattern.compile("[A-Za-z0-9._+-]+");
    }
    
    public MojangsonWriter(final Writer writer, final boolean pretty) {
        this.indent = 0;
        this.writer = Objects.requireNonNull(writer);
        this.pretty = pretty;
    }
    
    public MojangsonWriter(final Writer writer) {
        this(writer, false);
    }
    
    public void writeNamedTag(final String name, final NBTTag root) throws IOException {
        if (!name.isEmpty()) {
            this.write(new NBTString(name).toMSONString());
            this.write(58);
            if (this.pretty) {
                this.write(32);
            }
        }
        this.writeTag(root);
    }
    
    public void writeNamedTag(final NBTNamedTag nbt) throws IOException {
        this.writeNamedTag(nbt.getName(), nbt.getTag());
    }
    
    public void writeTag(final NBTTag tag) throws IOException {
        if (!this.pretty) {
            this.write(tag.toMSONString());
            return;
        }
        final NBTType type = tag.getType();
        if (type == NBTType.END || type.isPrimitive() || type.isArray()) {
            this.writer.write(tag.toMSONString());
        }
        else if (type == NBTType.COMPOUND) {
            this.writeCompound((NBTCompound)tag);
        }
        else {
            if (type != NBTType.LIST) {
                throw new AssertionError(type);
            }
            this.writeList((NBTList)tag);
        }
    }
    
    private void writeCompound(final NBTCompound compound) throws IOException {
        if (!this.pretty) {
            this.write(compound.toMSONString());
            return;
        }
        this.write(123);
        if (!compound.isEmpty()) {
            final boolean simple = isPrimitive(compound);
            if (!simple) {
                ++this.indent;
                this.endLn();
            }
            final Map<String, NBTTag> map = compound.getValue();
            final Set<String> keys = map.keySet();
            boolean first = true;
            if (simple) {
                for (final String key : keys) {
                    if (first) {
                        first = false;
                    }
                    else {
                        this.write(", ");
                    }
                    this.write(MojangsonWriter.SIMPLE_STRING.matcher(key).matches() ? key : NBTString.toMSONString(key));
                    this.write(": ");
                    this.writeTag(map.get(key));
                }
            }
            else {
                for (final String key : keys) {
                    if (first) {
                        first = false;
                    }
                    else {
                        this.write(",");
                        this.endLn();
                    }
                    this.write(MojangsonWriter.SIMPLE_STRING.matcher(key).matches() ? key : NBTString.toMSONString(key));
                    this.write(": ");
                    this.writeTag(map.get(key));
                }
            }
            if (!simple) {
                --this.indent;
                this.endLn();
            }
        }
        this.write(125);
    }
    
    private void writeList(final NBTList list) throws IOException {
        if (!this.pretty) {
            this.write(list.toMSONString());
            return;
        }
        this.write(91);
        if (!list.isEmpty()) {
            final boolean simple = isPrimitive(list);
            if (!simple) {
                ++this.indent;
                this.endLn();
            }
            boolean first = true;
            if (simple) {
                for (final NBTTag tag : list) {
                    if (first) {
                        first = false;
                    }
                    else {
                        this.write(", ");
                    }
                    this.writeTag(tag);
                }
            }
            else {
                for (final NBTTag tag : list) {
                    if (first) {
                        first = false;
                    }
                    else {
                        this.write(",");
                        this.endLn();
                    }
                    this.writeTag(tag);
                }
            }
            if (!simple) {
                --this.indent;
                this.endLn();
            }
        }
        this.write(93);
    }
    
    protected void indent() throws IOException {
        if (this.indent == 1) {
            this.writer.write("    ");
        }
        else if (this.indent > 0) {
            for (int i = 0; i < this.indent; ++i) {
                this.writer.append((CharSequence)"    ");
            }
        }
    }
    
    protected void endLn() throws IOException {
        this.writer.write(MojangsonWriter.NEWLINE);
        this.indent();
    }
    
    @Override
    public void write(final int c) throws IOException {
        this.writer.write(c);
    }
    
    @Override
    public void write(final String str) throws IOException {
        this.writer.write(str);
    }
    
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        this.writer.write(cbuf, off, len);
    }
    
    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }
    
    @Override
    public void close() throws IOException {
        this.writer.close();
    }
    
    private static boolean isPrimitive(final NBTList list) {
        return list.isEmpty() || list.getElementType().isPrimitive();
    }
    
    private static boolean isPrimitive(final NBTCompound compound) {
        if (compound.isEmpty()) {
            return true;
        }
        for (final NBTTag val : compound.getValue().values()) {
            if (!val.getType().isPrimitive()) {
                return false;
            }
        }
        return true;
    }
}
