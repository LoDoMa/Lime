package net.lodoma.lime.client.error;

import net.lodoma.lime.client.Client;

public interface ClientError
{
    public void onClientTermination(Client client);
}
