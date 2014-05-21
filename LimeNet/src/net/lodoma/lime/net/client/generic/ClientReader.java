package net.lodoma.lime.net.client.generic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

class ClientReader extends Thread
{
    private GenericClient client;
    
    public ClientReader(GenericClient client)
    {
        this.client = client;
    }
    
    public void run()
    {
        while (!this.isInterrupted())
        {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data,data.length);
            try
            {
                client.socket.receive(packet);
            }
            catch (IOException e)
            {
                if((e instanceof SocketException) && e.getMessage().equals("socket closed"))
                    break;
                client.handleException(e);
            }
            
            byte[] message = packet.getData();
            client.handler.handle(client, message);
        }
    }
}
