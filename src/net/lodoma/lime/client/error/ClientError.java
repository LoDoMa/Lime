package net.lodoma.lime.client.error;

import net.lodoma.lime.client.generic.net.GenericClient;

public interface ClientError
{
    public void onClientTermination(GenericClient client);
}
