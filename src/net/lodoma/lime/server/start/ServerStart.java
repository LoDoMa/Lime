package net.lodoma.lime.server.start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.lodoma.lime.server.Server;

public class ServerStart
{
    public static void main(String[] args)
    {
        Server server = new Server();
        server.open(19424);
        
        try
        {
            BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
            bR.readLine();
            bR.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        server.close();
    }
}