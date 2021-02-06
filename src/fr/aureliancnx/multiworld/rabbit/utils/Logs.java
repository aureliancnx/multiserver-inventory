// 
// Decompiled by Procyon v0.5.36
// 

package fr.aureliancnx.multiworld.rabbit.utils;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.io.IOException;
import java.io.File;
import fr.aureliancnx.multiworld.Multiworld;

public class Logs
{
    public static String name;
    
    public static void log(String message, final boolean server) {
        Logs.name = Logs.name.replace(":", "-");
        final File folder = new File(Multiworld.instance.getDataFolder(), "logs");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        final File file = new File(folder, String.valueOf(Logs.name) + ".log");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        final Calendar now = Calendar.getInstance();
        final String hr = (now.get(11) < 10) ? ("0" + now.get(11)) : Integer.toString(now.get(11));
        final String min = (now.get(12) < 10) ? ("0" + now.get(12)) : Integer.toString(now.get(12));
        final String sc = (now.get(13) < 10) ? ("0" + now.get(13)) : Integer.toString(now.get(13));
        message = "[" + hr + ":" + min + ":" + sc + "." + now.get(14) + "] " + message;
        try {
            final FileOutputStream fop = new FileOutputStream(file, true);
            fop.write(message.getBytes());
            fop.write(System.getProperty("line.separator").getBytes());
            fop.close();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        if (server) {
            System.out.println(message);
        }
    }
}
