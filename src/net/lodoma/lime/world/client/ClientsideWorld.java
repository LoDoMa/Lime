package net.lodoma.lime.world.client;

import java.util.List;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.world.CommonWorld;
import net.lodoma.lime.world.platform.Platform;

import org.lwjgl.opengl.GL11;

public class ClientsideWorld extends CommonWorld
{
    private Client client;
    private ChatConsole chatConsole;
    
    public ClientsideWorld(Client client)
    {
        this.client = client;
        this.chatConsole = new ChatConsole(this);
    }
    
    @Override
    public NetworkSide getNetworkSide()
    {
        return NetworkSide.CLIENT;
    }
    
    @Override
    public PropertyPool getPropertyPool()
    {
        return client;
    }
    
    public Client getClient()
    {
        return client;
    }
    
    public void generalInit()
    {
        ChatManager chatManager = (ChatManager) client.getProperty("chatManager");
        chatManager.addSender(chatConsole);
        chatManager.addReceiver(chatConsole);
    }
    
    public void update(double timeDelta)
    {
        super.update(timeDelta);
        chatConsole.update(timeDelta);
    }
    
    public void render()
    {
        GL11.glPushMatrix();
        GL11.glScalef(1.0f / 32.0f, 1.0f / 24.0f, 1.0f);
        
        List<Platform> platformList = getPlatformList();
        for(Platform platform : platformList)
            platform.render();
        
        List<Entity> entityList = getEntityList();
        for(Entity entity : entityList)
            entity.render();
        
        GL11.glPopMatrix();
        
        chatConsole.render();
    }
}
