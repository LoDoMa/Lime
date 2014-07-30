package net.lodoma.lime.server.io.chat;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;

public class SPChatMessageReceive extends ServerPacket
{
    public static final String NAME = "Lime::";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPChatMessageReceive(Server server)
    {
        super(server, HASH, String.class);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        user.outputStream.writeChars((String) args[0]);
        user.outputStream.writeChar(0);
    }
}
