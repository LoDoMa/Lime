package net.lodoma.lime.server.io.chat;

import java.io.IOException;
import java.util.List;

import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.logic.UserManager;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.HashPool32;

public class SIHChatMessageSend extends ServerInputHandler
{
    public static final String NAME = "Lime::ChatMessageSend";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SIHChatMessageSend(Server server)
    {
        super(server, NetStage.USER);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void localHandle(ServerUser user) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        char readChar;
        while((readChar = user.inputStream.readChar()) != 0)
        {
            builder.append(readChar);
        }
        
        String message = builder.toString();
        
        HashPool32<ServerOutput> soPool = (HashPool32<ServerOutput>) server.getProperty("soPool");
        ServerOutput output = soPool.get(SOChatMessageReceive.HASH);
        
        UserManager manager = (UserManager) server.getProperty("userManager");
        List<ServerUser> users = manager.getUserList();
        for(ServerUser userl : users)
            output.handle(userl, message);
    }
}
