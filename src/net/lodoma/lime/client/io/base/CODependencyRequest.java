package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;

public class CODependencyRequest extends ClientOutput
{
    public CODependencyRequest(Client client, String coName)
    {
        super(client, coName);
    }

    @Override
    protected void localHandle(Object... args) throws IOException
    {
        int index = (Integer) client.getProperty("dependencyProgress");
        outputStream.writeInt(index);
        client.setProperty("dependencyProgress", index + 1);
    }
}
