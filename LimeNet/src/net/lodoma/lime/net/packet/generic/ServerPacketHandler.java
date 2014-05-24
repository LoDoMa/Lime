package net.lodoma.lime.net.packet.generic;

import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;
import net.lodoma.lime.util.NetStage;

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
