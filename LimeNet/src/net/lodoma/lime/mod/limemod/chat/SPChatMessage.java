package net.lodoma.lime.mod.limemod.chat;

import net.lodoma.lime.net.packet.generic.ServerPacket;

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
