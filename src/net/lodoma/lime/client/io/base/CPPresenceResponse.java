package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.util.HashHelper;

public class CPPresenceResponse extends ClientPacket
{
    public static final String NAME = "Lime::PresenceResponse";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPPresenceResponse(Client client)
    {
        super(client, HASH);
    }

    @Override
    protected void localWrite(Object... args) throws IOException
    {
        
    }
}
