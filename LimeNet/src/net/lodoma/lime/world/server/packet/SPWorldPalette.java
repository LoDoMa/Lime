package net.lodoma.lime.world.server.packet;

import java.nio.ByteBuffer;
import java.util.Set;

import net.lodoma.lime.server.generic.net.packet.ServerPacket;
import net.lodoma.lime.world.ServersideWorld;
import net.lodoma.lime.world.material.Material;

public class SPWorldPalette extends ServerPacket
{
    public SPWorldPalette()
    {
        super(ServersideWorld.class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        ServersideWorld world = (ServersideWorld) args[0];
        Set<Short> paletteKeySet = world.getPaletteKeySet();
        ByteBuffer buffer = ByteBuffer.allocate(paletteKeySet.size() * 18 + 4);
        buffer.putInt(paletteKeySet.size());
        for(Short key : paletteKeySet)
        {
            Material material = world.getPaletteMember(key);
            buffer.putShort(key);
            buffer.putLong(material.uuid.getLeastSignificantBits());
            buffer.putLong(material.uuid.getMostSignificantBits());
        }
        return buffer.array();
    }
}
