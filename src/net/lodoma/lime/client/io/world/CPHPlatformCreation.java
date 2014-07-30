package net.lodoma.lime.client.io.world;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.client.ClientsideWorld;
import net.lodoma.lime.world.platform.Platform;

public class CPHPlatformCreation extends ClientPacketHandler
{
    public static final String NAME = "Lime::PlatformCreation";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHPlatformCreation(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        ((ClientsideWorld) client.getProperty("world")).addPlatform(new Platform(inputStream));
    }
}
