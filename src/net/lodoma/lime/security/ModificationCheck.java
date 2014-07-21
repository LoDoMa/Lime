package net.lodoma.lime.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.lodoma.lime.util.HashHelper;

public class ModificationCheck
{
    public static boolean isModified(File file, long expectedCRC) throws FileNotFoundException, IOException
    {
        if(!file.exists()) throw new FileNotFoundException();
        if(!file.isDirectory()) throw new IllegalArgumentException();
        return sumCRC(file) == expectedCRC;
    }
    
    public static long sumCRC(File file) throws FileNotFoundException, IOException
    {
        if(!file.exists()) throw new FileNotFoundException();
        if(!file.isDirectory()) throw new IllegalArgumentException();
        
        long crc = 0;
        
        File[] files = file.listFiles();
        for(File child : files)
        {
            if(child.isDirectory()) crc += sumCRC(child);
            else if(child.isFile()) crc += HashHelper.crcFromStream(new FileInputStream(child));
        }
        
        return crc;
    }
}
