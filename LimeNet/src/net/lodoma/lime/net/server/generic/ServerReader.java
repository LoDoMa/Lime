package net.lodoma.lime.net.server.generic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import net.lodoma.lime.net.packet.generic.ServerPacketPool;
import net.lodoma.lime.util.LogLevel;

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
            
            ByteBuffer buffer = ByteBuffer.wrap(message);
            long id = buffer.getLong();
            byte[] other = new byte[buffer.remaining()];
            buffer.get(other);
            
            ServerUser user = server.userPool.getUser(address, port);
            ServerPacketPool packetPool = (ServerPacketPool) server.getProperty("packetPool");
            packetPool.getHandler(id).handlePacket(server, user, other);
        }
    }
}
