package net.lodoma.lime.client.generic.net.packet;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.util.HashHelper;

public class ClientPacketPool
{
    private Map<Long, ClientPacket> packets;
    private Map<Long, ClientPacketHandler> handlers;
    
    public ClientPacketPool()
    {
        packets = new HashMap<Long, ClientPacket>();
        handlers = new HashMap<Long, ClientPacketHandler>();
    }
    
    private long getID(String packetName)
    {
        return HashHelper.hash64(packetName);
    }
    
    public void addPacket(String name, ClientPacket packet)
    {
        packets.put(getID(name), packet);
        packet.setID(getID(name));
    }
    
    public ClientPacket getPacket(String name)
    {
        return packets.get(getID(name));
    }
    
    public ClientPacket getPacket(long id)
    {
        return packets.get(id);
    }
    
    public void addHandler(String name, ClientPacketHandler handler)
    {
        handlers.put(getID(name), handler);
    }
    
    public ClientPacketHandler getHandler(String name)
    {
        return handlers.get(getID(name));
    }
    
    public ClientPacketHandler getHandler(long id)
    {
        return handlers.get(id);
    }
}