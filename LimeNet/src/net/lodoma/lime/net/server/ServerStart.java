package net.lodoma.lime.net.server;

import java.util.Scanner;

import net.lodoma.lime.net.server.generic.GenericServer;

public class ServerStart
{
    @SuppressWarnings("resource")
    public static void main(String[] args)
    {
        GenericServer server = new LimeServer();
        server.open(19523, new LimeServerLogic());
        
        new Scanner(System.in).nextLine();
        
        server.close();
    }
}
