package net.lodoma.lime.net.client;

import net.lodoma.lime.net.client.generic.GenericClient;

public class LimeClient extends GenericClient
{
    public void handleException(Exception exception)
    {
        exception.printStackTrace();
    }
}
