package net.lodoma.lime.client.io.base;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.security.ModificationCheck;
import net.lodoma.lime.util.HashPool;

public class CIHModificationCheck extends ClientInputHandler
{
    public CIHModificationCheck(Client client)
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
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    System.out.println("client closing - illegal file content");
                    client.close();
                }
            }).start();
            return;
        }
        
        ((HashPool<ClientOutput>) client.getProperty("coPool")).get("Lime::DependencyRequest").handle();
    }
}
