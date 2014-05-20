package net.lodoma.lime.packet;

import net.lodoma.lime.packet.generic.CommonHandler;

public final class LimeCommonHandler extends CommonHandler
{
    @Override
    public void loadPacketHandlers()
    {
        addPacketHandler(new ReportPacket());
    }
}
