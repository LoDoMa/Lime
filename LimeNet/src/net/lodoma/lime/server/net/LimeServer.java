package net.lodoma.lime.server.net;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.server.generic.GenericServer;

public class LimeServer extends GenericServer
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
