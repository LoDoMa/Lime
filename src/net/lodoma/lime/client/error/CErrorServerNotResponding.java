package net.lodoma.lime.client.error;

import net.lodoma.lime.client.Client;

public class CErrorServerNotResponding implements ClientError
{
    @Override
    public void onClientTermination(Client client)
    {
        System.out.println("[client shutdown]");
        System.out.println("server not responding");
    }
}
