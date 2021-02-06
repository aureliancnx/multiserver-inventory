// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt.io;

import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.io.InputStream;
import eisenwave.nbt.NBTNamedTag;
import eisenwave.io.Deserializer;

public class NBTDeserializer implements Deserializer<NBTNamedTag>
{
    private final boolean compressed;
    
    public NBTDeserializer(final boolean compressed) {
        this.compressed = compressed;
    }
    
    public NBTDeserializer() {
        this(true);
    }
    
    @Override
    public NBTNamedTag fromStream(final InputStream stream) throws IOException {
        final NBTInputStream nbtStream = this.compressed ? new NBTInputStream(new GZIPInputStream(stream)) : new NBTInputStream(stream);
        final NBTNamedTag tag = nbtStream.readNamedTag();
        if (tag == null) {
            throw new IOException("failed to read NBT tag due to EOS");
        }
        return tag;
    }
}
