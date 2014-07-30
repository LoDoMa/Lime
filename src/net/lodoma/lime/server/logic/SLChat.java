package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacketHandler;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.io.chat.SPHChatMessageSend;
import net.lodoma.lime.server.io.chat.SPChatMessageReceive;
import net.lodoma.lime.util.HashPool32;

public class SLChat implements ServerLogic
{
    private Server server;
    private HashPool32<ServerPacketHandler> sphPool;
    private HashPool32<ServerPacket> spPool;
    
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
        sphPool = (HashPool32<ServerPacketHandler>) server.getProperty("sphPool");
        spPool = (HashPool32<ServerPacket>) server.getProperty("spPool");
    }

    @Override
    public void generalInit()
    {
        sphPool.add(SPHChatMessageSend.HASH, new SPHChatMessageSend(server));
        spPool.add(SPChatMessageReceive.HASH, new SPChatMessageReceive(server));
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
