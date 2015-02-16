package net.lodoma.lime.server.start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.lodoma.lime.Lime;
import net.lodoma.lime.logger.LogLevel;
import net.lodoma.lime.server.Server;

public class ServerStart
{
    public static void main(String[] args)
    {
        Lime.init();
        Lime.LOGGER.setMinimumLevel(LogLevel.INFO);
        for (String arg : args)
            if (arg.equals("/F"))
                Lime.LOGGER.setMinimumLevel(LogLevel.FINEST);
        
        Server server = new Server();
        server.open();
        
        try(BufferedReader bR = new BufferedReader(new InputStreamReader(System.in)))
        {
            bR.readLine();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        server.close();
    }
}
