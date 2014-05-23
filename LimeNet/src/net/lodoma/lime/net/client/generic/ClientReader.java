package net.lodoma.lime.net.client.generic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.ByteBuffer;

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
            
            ByteBuffer buffer = ByteBuffer.wrap(message);
            int id = buffer.getInt();
            byte[] other = new byte[buffer.remaining()];
            buffer.get(other);
            
            ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
            packetPool.getHandler(id).handle(client, other);
        }
    }
}
