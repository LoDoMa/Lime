package net.lodoma.lime.net.server.generic;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.net.packet.generic.ServerPacket;
import net.lodoma.lime.net.packet.generic.ServerPacketHandler;

public class ServerPacketPool
{
    private Map<Integer, ServerPacket> packets;
    private Map<Integer, ServerPacketHandler> handlers;
    
    public ServerPacketPool()
    {
        packets = new HashMap<Integer, ServerPacket>();
        handlers = new HashMap<Integer, ServerPacketHandler>();
    }
    
    private int getID(String packetName)
    {
        return packetName.hashCode();
    }
    
    public void addPacket(String name, ServerPacket packet)
    {
        packets.put(getID(name), packet);
    }
    
    public ServerPacket getPacket(String name)
    {
        return packets.get(getID(name));
    }
    
    public ServerPacket getPacket(int id)
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
    
    public ServerPacketHandler getHandler(int id)
    {
        return handlers.get(id);
    }
}
