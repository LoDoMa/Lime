package net.lodoma.lime.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class HashHelper
{
    public static int hash32(String data)
    {
        return data.hashCode();
    };
    
    public static long hash64(String data)
    {
        long h = 1125899906842597L;
        int len = data.length();
        for (int i = 0; i < len; i++)
            h = 31 * h + data.charAt(i);
        return h;
    };
    
    public static long crcFromStream(InputStream stream) throws IOException
    {
        CRC32 crc = new CRC32();
        byte[] buffer = new byte[1024];
        int length;
        while((length = stream.read(buffer)) != -1)
            crc.update(buffer, 0, length);
        return crc.getValue();
    }
}
