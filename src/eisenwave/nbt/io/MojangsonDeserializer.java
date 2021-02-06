// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt.io;

import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.Reader;
import eisenwave.nbt.NBTNamedTag;
import eisenwave.io.TextDeserializer;

public class MojangsonDeserializer implements TextDeserializer<NBTNamedTag>
{
    @Override
    public NBTNamedTag fromReader(final Reader reader) throws IOException {
        final BufferedReader buffReader = (BufferedReader)((reader instanceof BufferedReader) ? reader : new BufferedReader(reader));
        final String mson = buffReader.lines().collect(Collectors.joining());
        return MojangsonParser.parse(mson);
    }
}
