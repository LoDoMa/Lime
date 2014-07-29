package net.lodoma.lime.server.io.chat;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;

public class SOChatMessageReceive extends ServerOutput
{
    public static final String NAME = "Lime::";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SOChatMessageReceive(Server server)
    {
        super(server, HASH, String.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        user.outputStream.writeChars((String) args[0]);
        user.outputStream.writeChar(0);
    }
}
