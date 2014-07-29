package net.lodoma.lime.client.io.base;

import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientOutput;
import net.lodoma.lime.util.HashHelper;

public class CODependencyRequest extends ClientOutput
{
    public static final String NAME = "Lime::DependencyRequest";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public CODependencyRequest(Client client)
    {
        super(client, HASH);
    }

    @Override
    protected void localHandle(Object... args) throws IOException
    {
        int index = (Integer) client.getProperty("dependencyProgress");
        outputStream.writeInt(index);
        client.setProperty("dependencyProgress", index + 1);
    }
}
