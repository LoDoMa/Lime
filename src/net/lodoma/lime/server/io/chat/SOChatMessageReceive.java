package net.lodoma.lime.server.io.chat;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;

public class SOChatMessageReceive extends ServerOutput
{
    public SOChatMessageReceive(Server server, String soName)
    {
        super(server, soName, String.class);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        user.outputStream.writeChars((String) args[0]);
        user.outputStream.writeChar(0);
    }
}
