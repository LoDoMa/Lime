package net.lodoma.lime.net.server;

import java.util.Scanner;

import net.lodoma.lime.net.packet.LimeCommonHandler;
import net.lodoma.lime.net.packet.generic.GenericCommonHandler;
import net.lodoma.lime.net.server.generic.GenericServer;

public class ServerStart
{
    @SuppressWarnings("resource")
    public static void main(String[] args)
    {
        GenericCommonHandler commonHandler = new LimeCommonHandler();
        
        GenericServer server = new LimeServer();
        server.open(19523, commonHandler, new LimeServerLogic());
        
        new Scanner(System.in).nextLine();
        
        server.close();
    }
}
