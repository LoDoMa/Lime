package net.lodoma.lime.net.packet.generic;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public abstract class GenericCommonHandler
{
    private final Map<Class<?>, Integer> handlerIDs = new HashMap<Class<?>, Integer>();
    private final Map<Integer, PacketHandler> handlers = new HashMap<Integer, PacketHandler>();
    
    public abstract void loadPacketHandlers();
    
    protected final void addPacketHandler(int id, PacketHandler handler)
    {
        handler.setID(id);
        if(!handlers.containsKey(id))
            handlers.put(id, handler);
        handlerIDs.put(handler.getClass(), id);
    }
    
    public final int getPacketHandlerID(Class<?> clazz)
    {
        return handlerIDs.get(clazz);
    }
    
    public final PacketHandler getPacketHandler(Class<?> packetHandlerClass)
    {
        return handlers.get(handlerIDs.get(packetHandlerClass));
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
