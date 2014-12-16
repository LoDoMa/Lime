package net.lodoma.lime.client.io.light;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.shader.light.BasicLight;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.client.ClientsideWorld;

public class CPHLightCreation extends ClientPacketHandler
{
    public static final String NAME = "Lime::LightCreation";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHLightCreation(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        int typeHash = inputStream.readInt();
        
        Light light = null;
        
        if (typeHash == BasicLight.HASH) light = new BasicLight(inputStream);
        
        if (light == null)
            // TOOD: improve this exception later
            throw new RuntimeException("invalid lighting type hash");
        
        ((ClientsideWorld) client.getProperty("world")).newLight(light);
    }
}
