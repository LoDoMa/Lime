package net.lodoma.lime.util;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Pipe
{
    private PipedInputStream inputStream;
    private PipedOutputStream outputStream;
    
    public Pipe() throws IOException
    {
        outputStream = new PipedOutputStream();
        inputStream = new PipedInputStream(outputStream);
    }
    
    public PipedInputStream getInputStream()
    {
        return inputStream;
    }
    
    public PipedOutputStream getOutputStream()
    {
        return outputStream;
    }
}
