package net.lodoma.lime.world.server.packet;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;
import net.lodoma.lime.server.generic.net.packet.ServerPacketHandler;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;
import net.lodoma.lime.world.server.ServersideWorld;

public class SPHWorldPaletteRequest extends ServerPacketHandler
{

    public SPHWorldPaletteRequest()
    {
        super(NetStage.USER);
    }

    @Override
    protected void handle(GenericServer server, ServerUser user, byte[] data)
    {
        ServerPacketPool packetPool = (ServerPacketPool) server.getProperty("packetPool");
        packetPool.getPacket("Lime::WorldPalette").send(server, user, (ServersideWorld) server.getProperty("world"));
    }
}
