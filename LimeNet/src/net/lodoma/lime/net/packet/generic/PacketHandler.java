package net.lodoma.lime.net.packet.generic;

import java.nio.ByteBuffer;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public abstract class PacketHandler
{
    public int ID;
    public final NetStage STAGE;
    
    public PacketHandler(NetStage stage)
    {
        STAGE = stage;
    }
    
    void setID(int id)
    {
        ID = id;
    }
    
    protected final byte[] buildMessage(byte[] data)
    {
        ByteBuffer buffer = ByteBuffer.allocate(data.length + 4);
        buffer.putInt(ID);
        buffer.put(data);
        return buffer.array();
    }
    
    public final void sendHeader(GenericClient client)
    {
        client.sendData(buildMessage(new byte[] {}));
    }
    
    public final void sendHeader(GenericServer server, ServerUser user)
    {
        server.sendData(buildMessage(new byte[] {}), user);
    }
    
    public abstract void handle(GenericClient client, byte[] data);
    public abstract void handle(GenericServer server, byte[] data, ServerUser user);
}
