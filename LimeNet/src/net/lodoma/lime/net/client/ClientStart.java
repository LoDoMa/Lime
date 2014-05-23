package net.lodoma.lime.net.client;

import net.lodoma.lime.net.packet.CPConnectRequest;
import net.lodoma.lime.net.packet.CPHConnectRequestAnswer;
import net.lodoma.lime.net.packet.dependencytest.CPHTestDependency;
import net.lodoma.lime.net.packet.dependencytest.CPHUserStatus;
import net.lodoma.lime.net.packet.dependencytest.CPTestDependencyAnswer;
import net.lodoma.lime.net.packet.generic.ClientPacketPool;


public class ClientStart
{
    public static void main(String[] args)
    {
        LimeClient client = new LimeClient();
        client.open(19523, "localhost", new LimeClientLogic());
        
        ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
        packetPool.addPacket("Lime::ConnectRequest", new CPConnectRequest());
        packetPool.addHandler("Lime::ConnectRequestAnswer", new CPHConnectRequestAnswer());
        packetPool.addPacket("Lime::DependencyRequest", new CPConnectRequest());
        packetPool.addPacket("Lime::TestDependencyAnswer", new CPTestDependencyAnswer());
        packetPool.addHandler("Lime::TestDependency", new CPHTestDependency());
        packetPool.addHandler("Lime::UserStatus", new CPHUserStatus());
        
        packetPool.getPacket("Lime::ConnectRequest").send(client);
    }
}
