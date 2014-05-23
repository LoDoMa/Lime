package net.lodoma.lime.net.packet.generic;

import net.lodoma.lime.net.server.generic.GenericServer;
import net.lodoma.lime.net.server.generic.ServerUser;

public abstract class ServerPacketHandler
{
    public abstract void handle(GenericServer server, ServerUser user, byte[] data);
}
