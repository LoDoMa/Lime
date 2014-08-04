package net.lodoma.lime.world.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.world.CommonWorld;

public class ClientsideWorld extends CommonWorld
{
    private Client client;
    
    private WorldRenderer renderer;
    private ChatConsole chatConsole;
    
    private Map<Integer, List<Light>> lights;
    
    public ClientsideWorld(Client client)
    {
        this.client = client;
        this.renderer = new WorldRenderer(this);
        this.chatConsole = new ChatConsole(this);
        
        this.lights = new HashMap<Integer, List<Light>>();
    }
    
    public synchronized void addLight(Light light)
    {
        int typeHash = light.getTypeHash();
        if(!lights.containsKey(typeHash))
            lights.put(typeHash, new ArrayList<Light>());
        lights.get(typeHash).add(light);
    }
    
    public synchronized Map<Integer, List<Light>> getLightMap()
    {
        return new HashMap<Integer, List<Light>>(lights);
    }
    
    public synchronized void removeLight(Light light)
    {
        long typeHash = light.getTypeHash();
        List<Light> lightList = lights.get(typeHash);
        if(lightList.size() == 1) lights.remove(typeHash);
        else lightList.remove(light);
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
