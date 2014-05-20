package net.lodoma.lime.server.generic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

import net.lodoma.lime.server.generic.GenericServer.LogLevel;

class ServerReader extends Thread
{
    private GenericServer server;
    
    public ServerReader(GenericServer server)
    {
        this.server = server;
    }
    
    public void run()
    {
        while (!this.isInterrupted())
        {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try
            {
                server.socket.receive(packet);
            }
            catch (IOException e)
            {
                if((e instanceof SocketException) && e.getMessage().equals("socket closed"))
                    break;
                server.log(LogLevel.SEVERE, e);
            }
            
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            byte[] message = packet.getData();
            
            ServerUser user = server.userPool.getUser(address, port);
            server.handler.handle(server, message, user);
        }
    }
}
