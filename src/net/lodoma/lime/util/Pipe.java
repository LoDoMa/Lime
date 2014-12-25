package net.lodoma.lime.util;

import java.io.IOException;

public class Pipe
{
    private FastPIS inputStream;
    private FastPOS outputStream;
    
    public Pipe() throws IOException
    {
        inputStream = new FastPIS();
        outputStream = new FastPOS(inputStream);
    }
    
    public FastPIS getInputStream()
    {
        return inputStream;
    }
    
    public FastPOS getOutputStream()
    {
        return outputStream;
    }
}
