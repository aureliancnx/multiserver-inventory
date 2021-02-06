// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.io;

import java.io.FileWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

public interface TextSerializer<T> extends Serializer<T>
{
    void toWriter(final T p0, final Writer p1) throws IOException;
    
    default char[] toCharArray(final T object) throws IOException {
        final CharArrayWriter writer = new CharArrayWriter();
        this.toWriter(object, writer);
        return writer.toCharArray();
    }
    
    default String toString(final T object) throws IOException {
        final Writer writer = new StringWriter();
        this.toWriter(object, writer);
        return writer.toString();
    }
    
    default void toStream(final T object, final OutputStream stream) throws IOException {
        final Writer writer = new OutputStreamWriter(stream);
        this.toWriter(object, writer);
        writer.flush();
    }
    
    default void toFile(final T object, final File file) throws IOException {
        Throwable t = null;
        try {
            final Writer writer = new FileWriter(file);
            try {
                this.toWriter(object, writer);
            }
            finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        finally {
            if (t == null) {
                final Throwable exception;
                t = exception;
            }
            else {
                final Throwable exception;
                if (t != exception) {
                    t.addSuppressed(exception);
                }
            }
        }
    }
}
