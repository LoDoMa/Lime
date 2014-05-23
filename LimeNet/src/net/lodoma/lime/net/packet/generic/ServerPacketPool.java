package net.lodoma.lime.net.packet.generic;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.util.HashHelper;

public class ServerPacketPool
{
    private Map<Long, ServerPacket> packets;
    private Map<Long, ServerPacketHandler> handlers;
    
    public ServerPacketPool()
    {
        packets = new HashMap<Long, ServerPacket>();
        handlers = new HashMap<Long, ServerPacketHandler>();
    }
    
    private long getID(String packetName)
    {
        return HashHelper.hash64(packetName);
    }
    
    public void addPacket(String name, ServerPacket packet)
    {
        packets.put(getID(name), packet);
        packet.setID(getID(name));
    }
    
    public ServerPacket getPacket(String name)
    {
        return packets.get(getID(name));
    }
    
    public ServerPacket getPacket(long id)
    {
        return packets.get(id);
    }
    
    public void addHandler(String name, ServerPacketHandler handler)
    {
        handlers.put(getID(name), handler);
    }
    
    public ServerPacketHandler getHandler(String name)
    {
        return handlers.get(getID(name));
    }
    
    public ServerPacketHandler getHandler(long id)
    {
        return handlers.get(id);
    }
}
