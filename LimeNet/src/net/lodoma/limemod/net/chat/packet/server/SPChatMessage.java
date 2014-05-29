package net.lodoma.limemod.net.chat.packet.server;

import net.lodoma.lime.server.generic.net.packet.ServerPacket;

public class SPChatMessage extends ServerPacket
{
    public SPChatMessage()
    {
        super(byte[].class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        return (byte[]) args[0];
    }
}
