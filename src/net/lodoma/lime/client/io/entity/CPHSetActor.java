package net.lodoma.lime.client.io.entity;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.util.HashHelper;
/**
 * CPHSetActor is a ClientPacketHandler
 * that handles the SPSetActor packet.
 * 
 * This handler modifies the "actor" property in client
 * to the received entityID.
 * 
 * @author Lovro Kalinovčić
 */
public class CPHSetActor extends ClientPacketHandler
{
    /**
     * Unique name of this handler
     */
    public static final String NAME = "Lime::SetActor";
    
    /**
     * 32-bit hash of this handlers name;
     */
    public static final int HASH = HashHelper.hash32(NAME);
    
    /**
     * 
     * @param client - the client that uses this handler
     */
    public CPHSetActor(Client client)
    {
        super(client);
    }
    
    @Override
    protected void localHandle() throws IOException
    {
        int entityID = inputStream.readInt();
        client.setProperty("actor", entityID);
    }
}
