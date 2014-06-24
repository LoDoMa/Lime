package net.lodoma.lime.client.error;

import net.lodoma.lime.client.generic.net.GenericClient;

public class CErrorServerNotResponding implements ClientError
{
    @Override
    public void onClientTermination(GenericClient client)
    {
        System.out.println("[client shutdown]");
        System.out.println("server not responding");
    }
}
