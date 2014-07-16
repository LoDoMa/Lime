package net.lodoma.lime.client.io.world;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;

public class COInitialWorldRequest extends ClientOutput
{
    public COInitialWorldRequest(Client client, String coName)
    {
        super(client, coName);
    }

    @Override
    protected void localHandle(Object... args) throws IOException
    {
        
    }
}
