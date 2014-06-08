package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.generic.net.ClientLogic;
import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.client.net.packet.CPConnectRequest;
import net.lodoma.lime.client.net.packet.CPDependencyRequest;
import net.lodoma.lime.client.net.packet.CPHConnectRequestAnswer;
import net.lodoma.lime.client.net.packet.CPHResponse;
import net.lodoma.lime.client.net.packet.CPHUserStatus;
import net.lodoma.lime.client.net.packet.CPResponseRequest;
import net.lodoma.lime.common.net.NetStage;

public class BaseLogic implements ClientLogic
{
    private GenericClient client;
    private ClientPacketPool packetPool;
    
    @Override
    public void baseInit(GenericClient client)
    {
        this.client = client;
    }

    @Override
    public void propertyInit()
    {
        client.setProperty("networkStage", NetStage.PRIMITIVE);
        client.setProperty("packetPool", new ClientPacketPool());
    }

    @Override
    public void fetchInit()
    {
        packetPool = (ClientPacketPool) client.getProperty("packetPool");
    }

    @Override
    public void generalInit()
    {
        packetPool.addPacket("Lime::ConnectRequest", new CPConnectRequest());
        packetPool.addHandler("Lime::ConnectRequestAnswer", new CPHConnectRequestAnswer());
        packetPool.addPacket("Lime::DependencyRequest", new CPDependencyRequest());
        packetPool.addHandler("Lime::UserStatus", new CPHUserStatus());
        packetPool.addPacket("Lime::ResponseRequest", new CPResponseRequest());
        packetPool.addHandler("Lime::Response", new CPHResponse());
    }
    
    private boolean first = true;
    
    @Override
    public void logic()
    {
        if(first)
        {
            packetPool.getPacket("Lime::ConnectRequest").send(client);
            first = false;
        }
    }
    
    @Override
    public void clean()
    {
        
    }
}
