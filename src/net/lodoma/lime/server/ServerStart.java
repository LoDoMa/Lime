package net.lodoma.lime.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.common.net.NetworkSettings;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.net.LimeServer;

public class ServerStart
{
    public static void main(String[] args)
    {
        GenericServer server = new LimeServer();
        server.open(NetworkSettings.SHADER_PORT);
        
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
