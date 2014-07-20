package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;

public class COPresenceResponse extends ClientOutput
{
    public COPresenceResponse(Client client, String coName)
    {
        super(client, coName);
    }

    @Override
    protected void localHandle(Object... args) throws IOException
    {
        
    }
}
