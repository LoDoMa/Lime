package net.lodoma.lime.server.generic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import net.lodoma.lime.common.net.LogLevel;
import net.lodoma.lime.common.net.NetworkSettings;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;

class ServerReader extends Thread
{
    private GenericServer server;
    private ServerPacketPool packetPool;
    private UserPool userPool;
    
    public ServerReader(GenericServer server)
    {
        this.server = server;
    }
    
    public void run()
    {
        packetPool = (ServerPacketPool) server.getProperty("packetPool");
        userPool = (UserPool) server.getProperty("userPool");
        
        while (!this.isInterrupted())
        {
            byte[] data = new byte[NetworkSettings.MAX_PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try
            {
                server.socket.receive(packet);
            }
            catch (IOException e)
            {
                if((e instanceof SocketException) && e.getMessage().toLowerCase().equals("socket closed"))
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
            
            packetPool.getHandler(id).handlePacket(server, userPool.getUser(address, port), other);
        }
    }
}
