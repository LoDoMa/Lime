package net.lodoma.lime.net.client;

import java.util.Scanner;


public class ClientStart
{
    @SuppressWarnings("resource")
    public static void main(String[] args)
    {
        LimeClient client = new LimeClient();
        client.open(19523, "localhost", new LimeClientLogic());
        
        new Scanner(System.in).nextLine();
        
        client.close();
    }
}
