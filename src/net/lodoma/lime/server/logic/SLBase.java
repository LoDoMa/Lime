package net.lodoma.lime.server.logic;

import net.lodoma.lime.common.net.packet.DependencyPool;
import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.UserPool;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;
import net.lodoma.lime.server.net.packet.SPConnectRequestAnswer;
import net.lodoma.lime.server.net.packet.SPHConnectRequest;
import net.lodoma.lime.server.net.packet.SPHDependencyRequest;
import net.lodoma.lime.server.net.packet.SPUserStatus;

public class SLBase implements ServerLogic
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
        server.setProperty("packetPool", new ServerPacketPool(server));
        server.setProperty("userPool", new UserPool());
        server.setProperty("dependencyPool", new DependencyPool());
    }
    
    @Override
    public void fetchInit()
    {
        packetPool = (ServerPacketPool) server.getProperty("packetPool");
    }
    
    @Override
    public void generalInit()
    {
        packetPool.addHandler("Lime::ConnectRequest", new SPHConnectRequest());
        packetPool.addPacket("Lime::ConnectRequestAnswer", new SPConnectRequestAnswer());
        packetPool.addHandler("Lime::DependencyRequest", new SPHDependencyRequest());
        packetPool.addPacket("Lime::UserStatus", new SPUserStatus());
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
