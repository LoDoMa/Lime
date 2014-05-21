package net.lodoma.lime.net.client;

import java.util.Scanner;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.LimeCommonHandler;
import net.lodoma.lime.net.packet.generic.GenericCommonHandler;

public class ClientStart
{
    @SuppressWarnings("resource")
    public static void main(String[] args)
    {
        GenericCommonHandler commonHandler = new LimeCommonHandler();
        
        GenericClient client = new LimeClient();
        client.open(19523, "localhost", commonHandler);
        
        if(client.hasProperty("accepted"))
            client.removeProperty("accepted");
        commonHandler.getPacketHandler(0).sendHeader(client);
        
        while(!client.hasProperty("accepted"))
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        boolean accepted = (Boolean) client.getProperty("accepted");
        if(accepted)
            System.out.println("connection accepted");
        else
            System.out.println("connection rejected");
        
        new Scanner(System.in).nextLine();
        
        client.close();
    }
}
