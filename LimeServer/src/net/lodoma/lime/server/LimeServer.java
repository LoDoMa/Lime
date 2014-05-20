package net.lodoma.lime.server;

import net.lodoma.lime.server.generic.GenericServer;

public class LimeServer extends GenericServer
{
    public void handleException(Exception exception)
    {
        exception.printStackTrace();
    }
}
