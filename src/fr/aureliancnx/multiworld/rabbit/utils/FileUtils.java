// 
// Decompiled by Procyon v0.5.36
// 

package fr.aureliancnx.multiworld.rabbit.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils
{
    public static byte[] readFile(final String filePath) throws Exception {
        return Files.readAllBytes(Paths.get(filePath, new String[0]));
    }
    
    public static void writeFile(final File file, final String str) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            final FileOutputStream fop = new FileOutputStream(file, false);
            fop.write(str.getBytes());
            fop.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
