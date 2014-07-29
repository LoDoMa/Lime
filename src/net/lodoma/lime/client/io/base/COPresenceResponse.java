package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.util.HashHelper;

public class COPresenceResponse extends ClientOutput
{
    public static final String NAME = "Lime::PresenceResponse";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public COPresenceResponse(Client client)
    {
        super(client, HASH);
    }

    @Override
    protected void localHandle(Object... args) throws IOException
    {
        
    }
}
