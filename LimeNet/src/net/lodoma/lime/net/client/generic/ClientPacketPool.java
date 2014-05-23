package net.lodoma.lime.net.client.generic;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.net.packet.generic.ClientPacket;
import net.lodoma.lime.net.packet.generic.ClientPacketHandler;

public class ClientPacketPool
{
    private Map<Integer, ClientPacket> packets;
    private Map<Integer, ClientPacketHandler> handlers;
    
    public ClientPacketPool()
    {
        packets = new HashMap<Integer, ClientPacket>();
        handlers = new HashMap<Integer, ClientPacketHandler>();
    }
    
    private int getID(String packetName)
    {
        return packetName.hashCode();
    }
    
    public void addPacket(String name, ClientPacket packet)
    {
        packets.put(getID(name), packet);
    }
    
    public ClientPacket getPacket(String name)
    {
        return packets.get(getID(name));
    }
    
    public ClientPacket getPacket(int id)
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
    
    public ClientPacketHandler getHandler(int id)
    {
        return handlers.get(id);
    }
}