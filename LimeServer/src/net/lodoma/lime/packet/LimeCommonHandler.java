package net.lodoma.lime.packet;

import net.lodoma.lime.packet.generic.GenericCommonHandler;

public final class LimeCommonHandler extends GenericCommonHandler
{
    @Override
    public void loadPacketHandlers()
    {
        addPacketHandler(new ReportPacket());
    }
}
