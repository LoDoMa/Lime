package net.lodoma.lime.net.packet.dependency;

import java.nio.ByteBuffer;

import net.lodoma.lime.mod.Dependency;
import net.lodoma.lime.net.packet.generic.ServerPacket;
import net.lodoma.lime.net.packet.generic.ServerPacketPool;

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
