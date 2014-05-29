package net.lodoma.lime.server.net.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.mod.targetserver.Dependency;
import net.lodoma.lime.server.generic.net.packet.ServerPacket;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;

@Dependency
public class SPModuleDependency extends ServerPacket
{
    private String name;
    
    public SPModuleDependency(String name)
    {
        super();
        this.name = name;
        setID(ServerPacketPool.getID("Lime::ModuleDependency"));
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4 + name.length());
        buffer.putInt(name.length());
        buffer.put(name.getBytes());
        return buffer.array();
    }
}
