package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.util.HashHelper;

public class CPHNetworkStageChange extends ClientPacketHandler
{
    public static final String NAME = "Lime::NetworkStageChange";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHNetworkStageChange(Client client)
    {
        super(client);
    }

    @Override
    protected void localHandle() throws IOException
    {
        int index = inputStream.readInt();
        client.setProperty("networkStage", NetStage.values()[index]);
    }
}
