package net.lodoma.lime.net.client;

import javax.swing.JOptionPane;

public class ClientStart
{
    public static void main(String[] args)
    {
        String ipName = JOptionPane.showInputDialog(null, "IP: ");
        
        LimeClient client = new LimeClient();
        client.open(19523, ipName, new LimeClientLogic());
    }
}
