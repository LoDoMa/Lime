package net.lodoma.lime.client.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.World;

public class CPHSnapshot extends ClientPacketHandler
{
    public static final String NAME = "Lime::Snapshot";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHSnapshot(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        int capacity = inputStream.readInt();
        byte[] bytes = new byte[capacity];
        inputStream.read(bytes);
        ByteBuffer snapshot = ByteBuffer.wrap(bytes);
        
        ((World) client.getProperty("world")).acceptSnapshot(snapshot);
    }
}