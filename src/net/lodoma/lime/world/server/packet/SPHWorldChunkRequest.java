package net.lodoma.lime.world.server.packet;

import java.nio.ByteBuffer;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;
import net.lodoma.lime.server.generic.net.packet.ServerPacketHandler;
import net.lodoma.lime.world.server.ServersideWorld;

public class SPHWorldChunkRequest extends ServerPacketHandler
{
    public SPHWorldChunkRequest()
    {
        super(NetStage.USER);
    }
    
    @Override
    protected void handle(GenericServer server, ServerUser user, byte[] data)
    {
        ServersideWorld world = (ServersideWorld) server.getProperty("world");
        ByteBuffer buffer = ByteBuffer.wrap(data);
        world.sendChunkPacket(user, buffer.getInt());
    }
}
