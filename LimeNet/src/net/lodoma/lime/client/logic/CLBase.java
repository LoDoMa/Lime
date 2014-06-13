package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.generic.net.GenericClient;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.client.net.packet.CPConnectRequest;
import net.lodoma.lime.client.net.packet.CPDependencyRequest;
import net.lodoma.lime.client.net.packet.CPHConnectRequestAnswer;
import net.lodoma.lime.client.net.packet.CPHUserStatus;
import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.texture.TexturePool;

public class CLBase implements ClientLogic
{
    private GenericClient client;
    private ClientPacketPool packetPool;
    private TexturePool texturePool;
    
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
        client.setProperty("texturePool", new TexturePool());
    }

    @Override
    public void fetchInit()
    {
        packetPool = (ClientPacketPool) client.getProperty("packetPool");
        texturePool = (TexturePool) client.getProperty("texturePool");
    }

    @Override
    public void generalInit()
    {
        packetPool.addPacket("Lime::ConnectRequest", new CPConnectRequest());
        packetPool.addHandler("Lime::ConnectRequestAnswer", new CPHConnectRequestAnswer());
        packetPool.addPacket("Lime::DependencyRequest", new CPDependencyRequest());
        packetPool.addHandler("Lime::UserStatus", new CPHUserStatus());

        texturePool.addTexture("Lime::test::dirt", new Texture("res/dirt.png"));
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
