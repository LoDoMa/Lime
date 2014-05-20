package net.lodoma.lime.client;

import net.lodoma.lime.client.generic.GenericClient;

public class LimeClient extends GenericClient
{
    public void handleException(Exception exception)
    {
        exception.printStackTrace();
    }
}
