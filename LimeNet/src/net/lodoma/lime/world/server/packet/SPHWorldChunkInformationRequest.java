package net.lodoma.lime.world.server.packet;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;
import net.lodoma.lime.server.generic.net.packet.ServerPacketHandler;
import net.lodoma.lime.world.server.ServersideWorld;

public class SPHWorldChunkInformationRequest extends ServerPacketHandler
{
    public SPHWorldChunkInformationRequest()
    {
        super(NetStage.USER);
    }

    @Override
    protected void handle(GenericServer server, ServerUser user, byte[] data)
    {
        ((ServersideWorld) server.getProperty("world")).sendChunkInformation(user);
    }
}
