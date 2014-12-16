package net.lodoma.lime.world.client;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.physics.VisualWorld;
import net.lodoma.lime.world.CommonWorld;

public class ClientsideWorld extends CommonWorld
{
    private Client client;
    
    private WorldRenderer renderer;
    private ChatConsole chatConsole;
    
    public ClientsideWorld(Client client)
    {
        visualWorld = new VisualWorld();
        
        this.client = client;
        this.renderer = new WorldRenderer(this);
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
        renderer.render();
        chatConsole.render();
    }
}
