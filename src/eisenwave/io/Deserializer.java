// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.io;

import java.net.URL;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Deserializer<T>
{
    T fromStream(final InputStream p0) throws IOException;
    
    default T fromFile(final File file) throws IOException {
        Throwable t = null;
        try {
            final InputStream stream = new FileInputStream(file);
            try {
                try {
                    final BufferedInputStream buffStream = new BufferedInputStream(stream);
                    try {
                        return this.fromStream(buffStream);
                    }
                    finally {
                        if (buffStream != null) {
                            buffStream.close();
                        }
                    }
                }
                finally {
                    return;
                }
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
    
    default T fromBytes(final byte[] bytes) throws IOException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return this.fromStream(stream);
    }
    
    default T fromResource(final Class<?> clazz, final String resPath) throws IOException {
        Throwable t = null;
        try {
            final InputStream stream = clazz.getClassLoader().getResourceAsStream(resPath);
            try {
                if (stream == null) {
                    throw new IOException("resource \"" + resPath + "\" could not be found");
                }
                return this.fromStream(stream);
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
    
    default T fromURL(final URL url) throws IOException {
        Throwable t = null;
        try {
            final InputStream stream = url.openStream();
            try {
                return this.fromStream(stream);
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
}
