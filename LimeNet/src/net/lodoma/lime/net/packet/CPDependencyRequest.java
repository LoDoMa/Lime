package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.packet.generic.ClientPacket;

public class CPDependencyRequest extends ClientPacket
{
    public CPDependencyRequest()
    {
        super(Boolean.class);
    }
    
    @Override
    protected byte[] build(Object... args)
    {
        return new byte[]
        { (byte) (((Boolean) args[0]) ? 0 : 1) };
    }
}
