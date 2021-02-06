// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt.io;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import java.io.OutputStream;
import eisenwave.nbt.NBTNamedTag;
import eisenwave.io.Serializer;

public class NBTSerializer implements Serializer<NBTNamedTag>
{
    private final boolean compress;
    
    public NBTSerializer(final boolean compress) {
        this.compress = compress;
    }
    
    public NBTSerializer() {
        this(true);
    }
    
    @Override
    public void toStream(final NBTNamedTag tag, final OutputStream stream) throws IOException {
        if (this.compress) {
            final GZIPOutputStream gzipStream = new GZIPOutputStream(stream);
            final NBTOutputStream nbtStream = new NBTOutputStream(gzipStream);
            nbtStream.writeNamedTag(tag);
            gzipStream.finish();
        }
        else {
            new NBTOutputStream(stream).writeNamedTag(tag);
        }
    }
}
