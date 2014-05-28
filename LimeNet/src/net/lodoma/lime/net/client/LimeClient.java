package net.lodoma.lime.net.client;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.lodoma.lime.net.LogLevel;
import net.lodoma.lime.net.client.generic.GenericClient;

public class LimeClient extends GenericClient
{
    @Override
    public void log(LogLevel level, String message)
    {
        PrintStream stream;
        if(level == LogLevel.WARNING || level == LogLevel.SEVERE)
            stream = System.err;
        else
            stream = System.out;
        stream.println(message);
    }
    
    public void log(LogLevel level, Exception exception)
    {
        StringWriter sWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(sWriter));
        log(level, exception.getMessage() + "\n" + sWriter.toString());
    }
}
