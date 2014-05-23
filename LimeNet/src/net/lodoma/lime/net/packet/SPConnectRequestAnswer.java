package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.packet.generic.ServerPacket;

public class SPConnectRequestAnswer extends ServerPacket
{
    public SPConnectRequestAnswer()
    {
        super(Boolean.class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        return new byte[]
        { (byte) (((Boolean) args[0]) ? 1 : 0) };
    }
}
