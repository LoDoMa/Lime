package net.lodoma.lime.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StringHelper
{
    private static void writeChar(char c, OutputStream os) throws IOException
    {
        os.write((c >>> 8) & 0xFF);
        os.write((c >>> 0) & 0xFF);
    }
    
    public static void write(String s, OutputStream os) throws IOException
    {
        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i++)
            writeChar(chars[i], os);
        writeChar((char) 0, os);
    }
    
    private static char readChar(InputStream is) throws IOException
    {
        int ch1 = is.read();
        int ch2 = is.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (char) ((ch1 << 8) + (ch2 << 0));
    }
    
    public static String read(InputStream is) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        char c;
        while((c = readChar(is)) != 0)
            builder.append(c);
        return builder.toString();
    }
}
