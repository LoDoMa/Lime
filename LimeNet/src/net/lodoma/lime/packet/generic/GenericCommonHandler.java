package net.lodoma.lime.packet.generic;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.client.generic.GenericClient;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;

public abstract class GenericCommonHandler
{
    private final Map<Integer, PacketHandler> handlers = new HashMap<Integer, PacketHandler>();
    
    public abstract void loadPacketHandlers();
    
    protected final void addPacketHandler(PacketHandler handler)
    {
        if(!handlers.containsKey(handler.ID))
            handlers.put(handler.ID, handler);
    }
    
    public final PacketHandler getPacketHandler(int ID)
    {
        return handlers.get(ID);
    }
    
    public final void handle(GenericClient client, byte[] data)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int packetHandlerID = buffer.getInt();
        byte[] remaining = new byte[buffer.remaining()];
        buffer.get(remaining);

        if(handlers.containsKey(packetHandlerID))
            handlers.get(packetHandlerID).handle(client, remaining);
    }
    
    public final void handle(GenericServer server, byte[] data, ServerUser user)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int packetHandlerID = buffer.getInt();
        byte[] remaining = new byte[buffer.remaining()];
        buffer.get(remaining);
        
        if(handlers.containsKey(packetHandlerID))
        {
            PacketHandler handler = handlers.get(packetHandlerID);
            if(handler.STAGE == user.stage)
                handler.handle(server, remaining, user);
        }
    }
}
