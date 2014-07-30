package net.lodoma.lime.client.io.base;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacketHandler;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.security.ModificationCheck;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.HashPool32;

public class CPHModificationCheck extends ClientPacketHandler
{
    public static final String NAME = "Lime::ModificationCheck";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CPHModificationCheck(Client client)
    {
        super(client);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void localHandle() throws IOException
    {
        long clientScript = ModificationCheck.sumCRC(new File("./script"));
        long clientModel = ModificationCheck.sumCRC(new File("./model"));
        
        long serverScript = inputStream.readLong();
        long serverModel = inputStream.readLong();
        
        if((clientScript != serverScript) || (clientModel != serverModel))
        {
            client.setCloseMessage("Illegal file content");
            client.closeInThread();
            return;
        }
        
        ((HashPool32<ClientPacket>) client.getProperty("cpPool")).get(CPDependencyRequest.HASH).write();
    }
}
