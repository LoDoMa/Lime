package net.lodoma.lime.net.packet.dependencytest;

import net.lodoma.lime.net.packet.generic.ClientPacket;

public class CPTestDependencyAnswer extends ClientPacket
{
    @Override
    protected byte[] build(Object... args)
    {
        return new byte[] {};
    }
}
