package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.ServerLogic;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;
import net.lodoma.lime.server.net.packet.SPHResponseRequest;
import net.lodoma.lime.server.net.packet.SPResponse;

public class SLConnectionCheck implements ServerLogic
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
        packetPool.addHandler("Lime::ResponseRequest", new SPHResponseRequest());
        packetPool.addPacket("Lime::Response", new SPResponse());
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
