// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt.io;

import java.io.StringWriter;
import java.io.IOException;
import java.io.Writer;
import eisenwave.nbt.NBTNamedTag;
import eisenwave.io.TextSerializer;

public class MojangsonSerializer implements TextSerializer<NBTNamedTag>
{
    private final boolean pretty;
    
    public MojangsonSerializer(final boolean pretty) {
        this.pretty = pretty;
    }
    
    public MojangsonSerializer() {
        this(false);
    }
    
    @Override
    public void toWriter(final NBTNamedTag nbt, final Writer writer) throws IOException {
        final MojangsonWriter msonWriter = new MojangsonWriter(writer, this.pretty);
        msonWriter.writeNamedTag(nbt);
        msonWriter.endLn();
    }
    
    @Override
    public String toString(final NBTNamedTag nbt) {
        final StringWriter stringWriter = new StringWriter();
        try {
            this.toWriter(nbt, (Writer)stringWriter);
        }
        catch (IOException e) {
            throw new AssertionError((Object)e);
        }
        return stringWriter.toString();
    }
}
