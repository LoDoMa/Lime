package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.util.HashHelper;

/**
 * CPDependencyRequest is a ClientPacket sent
 * as a request for a series of dependency packets.
 * 
 * SPHDependencyRequest is the SPH for this packet.
 * 
 * @author Lovro Kalinovčić
 */
public class CPDependencyRequest extends ClientPacket
{
    /**
     * Unique name of this packet
     */
    public static final String NAME = "Lime::DependencyRequest";
    
    /**
     * 32-bit hash of this packets name;
     */
    public static final int HASH = HashHelper.hash32(NAME);
    
    /**
     * 
     * @param client - the client that uses this packet
     */
    public CPDependencyRequest(Client client)
    {
        super(client, HASH);
    }

    @Override
    protected void localWrite(Object... args) throws IOException
    {
        int index = (Integer) client.getProperty("dependencyProgress");
        outputStream.writeInt(index);
        client.setProperty("dependencyProgress", index + 1);
    }
}
