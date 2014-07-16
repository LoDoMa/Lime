package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.common.net.NetStage;

public class CIHNetworkStageChange extends ClientInputHandler
{
    public CIHNetworkStageChange(Client client)
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