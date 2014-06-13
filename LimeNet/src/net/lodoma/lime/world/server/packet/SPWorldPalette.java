package net.lodoma.lime.world.server.packet;

import net.lodoma.lime.server.generic.net.packet.ServerPacket;
import net.lodoma.lime.world.ServersideWorld;

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
        // TODO: finish
        return new byte[] {};
    }
}
