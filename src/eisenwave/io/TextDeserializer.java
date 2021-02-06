// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.io;

import java.io.FileReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.StringReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;

public interface TextDeserializer<T> extends Deserializer<T>
{
    T fromReader(final Reader p0) throws IOException;
    
    default T fromCharArray(final char[] chars) throws IOException {
        return this.fromReader(new CharArrayReader(chars));
    }
    
    default T fromString(final String str) throws IOException {
        return this.fromReader(new StringReader(str));
    }
    
    default T fromStream(final InputStream stream) throws IOException {
        Throwable t = null;
        try {
            final Reader reader = new InputStreamReader(stream);
            try {
                return this.fromReader(reader);
            }
            finally {
                if (reader != null) {
                    reader.close();
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
    
    default T fromFile(final File file) throws IOException {
        Throwable t = null;
        try {
            final Reader reader = new FileReader(file);
            try {
                return this.fromReader(reader);
            }
            finally {
                if (reader != null) {
                    reader.close();
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
