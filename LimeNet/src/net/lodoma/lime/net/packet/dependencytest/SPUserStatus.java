package net.lodoma.lime.net.packet.dependencytest;

import net.lodoma.lime.net.packet.generic.ServerPacket;
import net.lodoma.lime.net.server.generic.NetStage;
import net.lodoma.lime.net.server.generic.ServerUser;
import net.lodoma.lime.net.server.generic.UserPool;

public class SPUserStatus extends ServerPacket
{
    public SPUserStatus()
    {
        super(ServerUser.class, UserPool.class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        ((ServerUser) args[0]).stage = NetStage.USER;
        ((UserPool) args[1]).getWaitingUsers().remove((ServerUser) args[0]);
        return new byte[] {};
    }
}
