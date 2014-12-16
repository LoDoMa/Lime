package net.lodoma.lime.server.io.light;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.util.HashHelper;

public class SPLightCreation extends ServerPacket
{
    public static final String NAME = "Lime::LightCreation";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPLightCreation(Server server)
    {
        super(server, HASH, Light.class);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        Light light = (Light) args[0];
        user.outputStream.writeInt(light.getTypeHash());
        light.write(user.outputStream);
    }
}
