package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.client.ClientsideWorld;

/**
 * CPHEntityCreation is a ClientPacketHandler
 * that handles the SPEntityCreation packet.
 * 
 * This handler receives the type hash
 * and creates a new entity of that type.
 * 
 * @author Lovro Kalinovčić
 */
public class CPHEntityCreation extends ClientPacketHandler
{
    /**
     * Unique name of this handler
     */
    public static final String NAME = "Lime::EntityCreation";
    
    /**
     * 32-bit hash of this handlers name;
     */
    public static final int HASH = HashHelper.hash32(NAME);
    
    /**
     * 
     * @param client - the client that uses this handler
     */
    public CPHEntityCreation(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        int typeHash = inputStream.readInt();
        ((ClientsideWorld) client.getProperty("world")).newEntity(typeHash);
    }
}
