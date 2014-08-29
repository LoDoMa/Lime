package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;

public class CPHEntityTransformModification extends ClientPacketHandler
{
    public static final String NAME = "Lime::EntityTransformModification";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHEntityTransformModification(Client client)
    {
        super(client);
    }
    
    @SuppressWarnings("unused")
    @Override
    protected void localHandle() throws IOException
    {
        int entityID = inputStream.readInt();
        int bodyID = inputStream.readInt();
        
        
    }
}
