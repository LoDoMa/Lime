package net.lodoma.lime;

import java.net.UnknownHostException;

import net.lodoma.lime.client.LimeClient;
import net.lodoma.lime.client.generic.GenericClient;
import net.lodoma.lime.packet.LimeCommonHandler;
import net.lodoma.lime.packet.generic.GenericCommonHandler;
import net.lodoma.lime.server.LimeServer;
import net.lodoma.lime.server.generic.GenericServer;

public class Test
{
    public static void main(String[] args) throws UnknownHostException, InterruptedException
    {
        GenericCommonHandler commonHandler = new LimeCommonHandler();
        
        GenericServer server = new LimeServer();
        server.open(19523, commonHandler);
        
        GenericClient client = new LimeClient();
        client.open(19523, "localhost", commonHandler);
        
        commonHandler.getPacketHandler(0).sendEmpty(client);
        
        Thread.sleep(1000);
        
        client.close();
        server.close();
    }
}
