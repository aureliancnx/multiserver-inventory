// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.io;

import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface Serializer<T>
{
    void toStream(final T p0, final OutputStream p1) throws IOException;
    
    default void toFile(final T object, final File file) throws IOException {
        Throwable t = null;
        try {
            final FileOutputStream stream = new FileOutputStream(file);
            try {
                try {
                    final BufferedOutputStream buffStream = new BufferedOutputStream(stream);
                    try {
                        this.toStream(object, buffStream);
                    }
                    finally {
                        if (buffStream != null) {
                            buffStream.close();
                        }
                    }
                }
                finally {}
            }
            finally {
                if (stream != null) {
                    stream.close();
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
    
    default byte[] toBytes(final T object, final int capacity) throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream(capacity);
        this.toStream(object, stream);
        stream.close();
        return stream.toByteArray();
    }
    
    default byte[] toBytes(final T object) throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.toStream(object, stream);
        stream.close();
        return stream.toByteArray();
    }
}
