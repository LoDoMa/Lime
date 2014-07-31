package net.lodoma.lime.server.io.chat;

import java.io.IOException;
import java.util.Set;

import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerPacketHandler;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.server.logic.UserManager;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.HashPool32;

public class SPHChatMessageSend extends ServerPacketHandler
{
    public static final String NAME = "Lime::ChatMessageSend";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPHChatMessageSend(Server server)
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
        
        HashPool32<ServerPacket> spPool = (HashPool32<ServerPacket>) server.getProperty("spPool");
        ServerPacket output = spPool.get(SPChatMessageReceive.HASH);
        
        UserManager manager = (UserManager) server.getProperty("userManager");
        Set<ServerUser> users = manager.getUserSet();
        for(ServerUser userl : users)
            output.write(userl, message);
    }
}
