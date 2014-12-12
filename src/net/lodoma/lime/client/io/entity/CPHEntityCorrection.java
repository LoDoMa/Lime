package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.physics.Entity;
import net.lodoma.lime.physics.EntityCorrector;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.CommonWorld;

/**
 * CPHEntityCorrection is a ClientPacketHandler
 * that handles the SPEntityCorrection packet.
 * 
 * This handler receives the entityID of an entity and
 * invokes the "receiveCorrection" method for it.
 * 
 * @author Lovro Kalinovčić
 */
public class CPHEntityCorrection extends ClientPacketHandler
{
    /**
     * Unique name of this handler
     */
    public static final String NAME = "Lime::EntityCorrection";
    
    /**
     * 32-bit hash of this handlers name;
     */
    public static final int HASH = HashHelper.hash32(NAME);
    
    /**
     * 
     * @param client - the client that uses this handler
     */
    public CPHEntityCorrection(Client client)
    {
        super(client);
    }

    @Override
    protected void localHandle() throws IOException
    {
        int entityCount = inputStream.readInt();
        
        while (entityCount-- > 0)
        {
            int identifier = inputStream.readInt();
            Entity entity = ((CommonWorld) client.getProperty("world")).entityPool.get(identifier);
            EntityCorrector.applyCorrection(entity, inputStream);
        }
    }
}
