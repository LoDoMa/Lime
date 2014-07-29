package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerInputHandler;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.io.chat.SIHChatMessageSend;
import net.lodoma.lime.server.io.chat.SOChatMessageReceive;
import net.lodoma.lime.util.HashPool32;

public class SLChat implements ServerLogic
{
    private Server server;
    private HashPool32<ServerInputHandler> sihPool;
    private HashPool32<ServerOutput> soPool;
    
    @Override
    public void baseInit(Server server)
    {
        this.server = server;
    }

    @Override
    public void propertyInit()
    {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fetchInit()
    {
        sihPool = (HashPool32<ServerInputHandler>) server.getProperty("sihPool");
        soPool = (HashPool32<ServerOutput>) server.getProperty("soPool");
    }

    @Override
    public void generalInit()
    {
        sihPool.add(SIHChatMessageSend.HASH, new SIHChatMessageSend(server));
        soPool.add(SOChatMessageReceive.HASH, new SOChatMessageReceive(server));
    }

    @Override
    public void clean()
    {
        
    }

    @Override
    public void logic()
    {
        
    }
}
