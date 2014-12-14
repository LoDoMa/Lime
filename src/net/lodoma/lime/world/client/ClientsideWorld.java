package net.lodoma.lime.world.client;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.physics.VisualWorld;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.world.CommonWorld;

public class ClientsideWorld extends CommonWorld
{
    private Client client;
    
    private WorldRenderer renderer;
    private ChatConsole chatConsole;
    
    private Map<Integer, Light> lights;
    
    public ClientsideWorld(Client client)
    {
        visualWorld = new VisualWorld();
        
        this.client = client;
        this.renderer = new WorldRenderer(this);
        this.chatConsole = new ChatConsole(this);
        
        this.lights = new HashMap<Integer, Light>();
    }
    
    public synchronized void addLight(int hash, Light light)
    {
        lights.put(hash, light);
    }
    
    public synchronized Light getLight(int hash)
    {
        return lights.get(hash);
    }
    
    public synchronized Map<Integer, Light> getLightMap()
    {
        return new HashMap<Integer, Light>(lights);
    }
    
    public synchronized void removeLight(int hash)
    {
        lights.remove(hash);
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
