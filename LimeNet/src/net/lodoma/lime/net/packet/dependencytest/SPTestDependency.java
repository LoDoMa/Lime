package net.lodoma.lime.net.packet.dependencytest;

import net.lodoma.lime.mod.Dependency;
import net.lodoma.lime.net.packet.generic.ServerPacket;

@Dependency
public class SPTestDependency extends ServerPacket
{
    @Override
    protected byte[] build(Object... args)
    {
        return new byte[] {};
    }
}
