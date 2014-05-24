package net.lodoma.lime.net.client;

import java.util.Scanner;

public class ClientStart
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String ipName = scanner.nextLine();
        scanner.close();
        
        LimeClient client = new LimeClient();
        client.open(19523, ipName, new LimeClientLogic());
    }
}
