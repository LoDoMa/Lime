package net.lodoma.lime.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.lodoma.lime.net.LogLevel;
import net.lodoma.lime.net.server.generic.GenericServer;

public class ServerStart
{
    public static void main(String[] args)
    {
        GenericServer server = new LimeServer();
        server.open(19523, new LimeServerLogic());
        
        try
        {
            BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
            bR.readLine();
            bR.close();
        }
        catch(IOException e)
        {
            server.log(LogLevel.SEVERE, e);
        }
        
        server.close();
    }
}
