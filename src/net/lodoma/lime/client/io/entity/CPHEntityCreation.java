package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;

public class CPHEntityCreation extends ClientPacketHandler
{
    public static final String NAME = "Lime::EntityCreation";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHEntityCreation(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        
    }
}
