package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.util.HashHelper;

/**
 * CPPresenceResponse is a ClientPacket sent
 * when nothing else has been sent to the server for some time.
 * The packet itself doesn't contain anything.
 * 
 * Sending this packet prevents the server from listing the client
 * as inactive.
 * 
 * This packet does not have a SPH yet.
 * TODO: modify when created
 * 
 * @author Lovro Kalinovčić
 */
public class CPPresenceResponse extends ClientPacket
{
    /**
     * Unique name of this packet
     */
    public static final String NAME = "Lime::PresenceResponse";
    
    /**
     * 32-bit hash of this packets name;
     */
    public static final int HASH = HashHelper.hash32(NAME);
    
    /**
     * 
     * @param client - the client that uses this packet
     */
    public CPPresenceResponse(Client client)
    {
        super(client, HASH);
    }

    @Override
    protected void localWrite(Object... args) throws IOException
    {
        
    }
}
