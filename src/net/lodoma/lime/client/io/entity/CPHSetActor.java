package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;

public class CPHSetActor extends ClientPacketHandler
{
    public static final String NAME = "Lime::SetActor";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHSetActor(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        int entityID = inputStream.readInt();
        client.setProperty("actor", entityID);
    }
}
