package net.lodoma.lime.net.packet;

import net.lodoma.lime.net.packet.generic.GenericCommonHandler;

public final class LimeCommonHandler extends GenericCommonHandler
{
    @Override
    public void loadPacketHandlers()
    {
        addPacketHandler(0, new LoginPacket());
        addPacketHandler(1, new VisualInstanceReadyPacket());
        addPacketHandler(2, new ConnectionCheckPacket());
    }
}
