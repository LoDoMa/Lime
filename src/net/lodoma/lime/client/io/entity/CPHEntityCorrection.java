package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityCorrector;
import net.lodoma.lime.util.HashHelper;

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
        int id = inputStream.readInt();
        Entity entity = null;
        EntityCorrector.receiveCorrection(entity, inputStream);
    }
}
