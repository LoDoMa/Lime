package net.lodoma.lime.server.logic;

import net.lodoma.lime.chat.packet.server.SPChatMessage;
import net.lodoma.lime.chat.packet.server.SPHChatMessage;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;

public class SLChat implements ServerLogic
{
    private GenericServer server;
    private ServerPacketPool packetPool;
    
    @Override
    public void baseInit(GenericServer server)
    {
        this.server = server;
    }

    @Override
    public void propertyInit()
    {
        
    }

    @Override
    public void fetchInit()
    {
        packetPool = (ServerPacketPool) server.getProperty("packetPool");
    }

    @Override
    public void generalInit()
    {
        packetPool.addPacket("Lime::ChatMessage", new SPChatMessage());
        packetPool.addHandler("Lime::ChatMessage", new SPHChatMessage());
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
