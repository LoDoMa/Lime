package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.packet.generic.GenericCommonHandler;

public final class LimeCommonHandler extends GenericCommonHandler
{
    @Override
    public void loadPacketHandlers()
    {
        addPacketHandler(new LoginPacket());
        addPacketHandler(new VisualInstanceReadyPacket());
    }
}
