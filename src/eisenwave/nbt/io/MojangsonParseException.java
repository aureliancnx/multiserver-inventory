// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt.io;

import java.io.IOException;

public class MojangsonParseException extends IOException
{
    public MojangsonParseException(final String msg, final String content, final int index) {
        super(String.valueOf(msg) + " at: " + printErrorLoc(content, index));
    }
    
    private static String printErrorLoc(final String content, final int index) {
        final StringBuilder builder = new StringBuilder();
        final int i = Math.min(content.length(), index);
        if (i > 35) {
            builder.append("...");
        }
        builder.append(content.substring(Math.max(0, i - 35), i));
        builder.append("<--[HERE]");
        return builder.toString();
    }
}
