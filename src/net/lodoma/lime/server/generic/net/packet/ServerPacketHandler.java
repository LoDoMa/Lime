package net.lodoma.lime.server.generic.net.packet;

import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerUser;

public abstract class ServerPacketHandler
{
    private NetStage stage;
    
    public ServerPacketHandler(NetStage stage)
    {
        this.stage = stage;
    }
    
    protected abstract void handle(GenericServer server, ServerUser user, byte[] data);
    
    public final void handlePacket(GenericServer server, ServerUser user, byte[] data)
    {
        if(user.stage != stage)
        {
            System.out.println("no permission");
            return;
        }
        handle(server, user, data);
    }
}