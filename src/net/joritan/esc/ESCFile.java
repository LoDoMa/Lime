package net.joritan.esc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ESCFile
{
    private File file;

    public ESCFile(File file)
    {
        this.file = file;
    }

    public ESCInputStream getInputStream()
    {
        try
        {
            return new ESCInputStream(new GZIPInputStream(new FileInputStream(file)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public ESCOutputStream getOutputStream()
    {
        try
        {
            return new ESCOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
