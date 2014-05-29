package net.lodoma.lime.server.generic.net.packet;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.common.net.packet.DependencyPool;
import net.lodoma.lime.mod.targetserver.Dependency;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.util.AnnotationHelper;
import net.lodoma.lime.util.HashHelper;

public class ServerPacketPool
{
    private GenericServer server;
    private Map<Long, ServerPacket> packets;
    private Map<Long, ServerPacketHandler> handlers;
    
    public ServerPacketPool(GenericServer server)
    {
        this.server = server;
        packets = new HashMap<Long, ServerPacket>();
        handlers = new HashMap<Long, ServerPacketHandler>();
    }
    
    public static long getID(String packetName)
    {
        return HashHelper.hash64(packetName);
    }
    
    public void addPacket(String name, ServerPacket packet)
    {
        long id = getID(name);
        packets.put(id, packet);
        packet.setID(id);
        if(AnnotationHelper.isAnnotationPresent(packet.getClass(), Dependency.class))
            ((DependencyPool) server.getProperty("dependencyPool")).addDependency(packet);
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
