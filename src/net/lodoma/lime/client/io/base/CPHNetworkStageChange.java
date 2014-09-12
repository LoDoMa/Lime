package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.util.HashHelper;

/**
 * CPHNetworkStageChange is a ClientPacketHandler
 * that handles the SPNetworkStateChange packet.
 * 
 * This handler modifies the "networkStage" property in client
 * to the received NetStage.
 * 
 * @author Lovro Kalinovčić
 */
public class CPHNetworkStageChange extends ClientPacketHandler
{
    /**
     * Unique name of this handler
     */
    public static final String NAME = "Lime::NetworkStageChange";
    
    /**
     * 32-bit hash of this handlers name;
     */
    public static final int HASH = HashHelper.hash32(NAME);

    /**
     * 
     * @param client - the client that uses this handler
     */
    public CPHNetworkStageChange(Client client)
    {
        super(client);
    }

    @Override
    protected void localHandle() throws IOException
    {
        int index = inputStream.readInt();
        client.setProperty("networkStage", NetStage.values()[index]);
    }
}
