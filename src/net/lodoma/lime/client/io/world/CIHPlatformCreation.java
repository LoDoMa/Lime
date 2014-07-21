package net.lodoma.lime.client.io.world;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.world.client.ClientsideWorld;
import net.lodoma.lime.world.platform.Platform;

public class CIHPlatformCreation extends ClientInputHandler
{
    public CIHPlatformCreation(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        ((ClientsideWorld) client.getProperty("world")).addPlatform(new Platform(inputStream));
    }
}
