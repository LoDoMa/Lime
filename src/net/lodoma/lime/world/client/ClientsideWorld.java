package net.lodoma.lime.world.client;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.chat.ChatReceiver;
import net.lodoma.lime.chat.ChatSender;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.common.NetworkSide;
import net.lodoma.lime.gui.ActiveTextField;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityWorld;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.SystemHelper;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.world.platform.Platform;

import org.lwjgl.opengl.GL11;

public class ClientsideWorld implements EntityWorld, ChatSender, ChatReceiver
{
    private Client client;
    
    private PhysicsWorld physicsWorld;
    private List<Platform> platforms;
    private Map<Integer, Entity> entities;

    private GUIContainer gui;
    
    private ChatManager chatManager;
    private boolean inChat;
    private ActiveTextField chatField;
    private List<ChatManager> chatManagers;
    private List<String> chatToSend;
    
    private List<Text> chatTexts;
    private Map<Text, Double> enterTimes;
    
    public ClientsideWorld(Client client)
    {
        this.client = client;
        
        physicsWorld = new PhysicsWorld();
        platforms = new ArrayList<Platform>();
        entities = new HashMap<Integer, Entity>();
        gui = new GUIContainer();
        
        chatManagers = new ArrayList<ChatManager>();
        chatToSend = new ArrayList<String>();
        
        chatTexts = new ArrayList<Text>();
        enterTimes = new HashMap<Text, Double>();
    }
    
    @Override
    public NetworkSide getNetworkSide()
    {
        return NetworkSide.CLIENT;
    }
    
    public Client getClient()
    {
        return client;
    }
    
    public void clean()
    {
        for(Platform platform : platforms)
            platform.destroy(physicsWorld);
        platforms.clear();
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            if(entity.isCreated())
                entity.destroy(physicsWorld);
        entities.clear();
    }
    
    public void fetchInit()
    {
        chatManager = (ChatManager) client.getProperty("chatManager");
    }
    
    public void generalInit()
    {
        chatManager.addSender(this);
        chatManager.addReceiver(this);
    }
    
    public PhysicsWorld getPhysicsWorld()
    {
        return physicsWorld;
    }
    
    public void addPlatform(Platform platform)
    {
        platform.create(physicsWorld);
        platforms.add(platform);
    }
    
    public List<Platform> getPlatformList()
    {
        return platforms;
    }
    
    public void removePlatform(Platform platform)
    {
        platforms.remove(platform);
    }
    
    @Override
    public void addEntity(Entity entity)
    {
        entity.create(physicsWorld);
        entities.put(entity.getID(), entity);
    }

    @Override
    public Entity getEntity(int id)
    {
        return entities.get(id);
    }
    
    public Set<Integer> getEntityIDSet()
    {
        return new HashSet<Integer>(entities.keySet());
    }

    @Override
    public void removeEntity(int id)
    {
        entities.remove(id);
    }
    
    @Override
    public void addChatManager(ChatManager manager)
    {
        chatManagers.add(manager);
    }
    
    @Override
    public void update(ChatManager manager)
    {
        String username = ((Credentials) client.getProperty("credentials")).getUsername();
        for(String message : chatToSend)
            for(ChatManager cmanager : chatManagers)
                cmanager.send(username + ": " + message);
        chatToSend.clear();
    }
    
    @Override
    public void receiveChat(ChatManager manager, String message)
    {
        Text text = new Text(0.02f, 0.2f, 0.02f, 0.025f, message, new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_LEFT);
        chatTexts.add(text);
        enterTimes.put(text, SystemHelper.getTimeNanos() / 1000000000.0);
    }
    
    @Override
    public void removeChatManager(ChatManager manager)
    {
        chatManagers.remove(manager);
    }
    
    public void update(double timeDelta)
    {
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            if(entity.isCreated())
                entity.update(timeDelta);
        physicsWorld.update(timeDelta);
        
        gui.update(timeDelta);

        if(inChat)
        {
            if(Input.getKeyDown(Input.KEY_RETURN))
            {
                String message = chatField.getText();
                chatToSend.add(message);
                gui.removeElement(chatField);
                inChat = false;
            }
        }
        if(!inChat && Input.getKeyDown(Input.KEY_T))
        {
            chatField = new ActiveTextField(new Rectangle(0.0f, 0.1f, 1.0f, 0.033f));
            gui.addElement(chatField);
            inChat = true;
        }
        
        List<Text> chatToRemove = new ArrayList<Text>();
        for(Text text : chatTexts)
        {
            double enterTime = enterTimes.get(text);
            double currentTime = SystemHelper.getTimeNanos() / 1000000000.0f;
            if(currentTime - enterTime >= 5)
                chatToRemove.add(text);
        }
        chatTexts.removeAll(chatToRemove);
        for(Text toRemove : chatToRemove)
            enterTimes.remove(toRemove);
    }
    
    public void render()
    {
        GL11.glPushMatrix();
        GL11.glScalef(1.0f / 16.0f, 1.0f / 12.0f, 1.0f);
        
        for(Platform platform : platforms)
            platform.render();
        
        List<Entity> entityList = new ArrayList<Entity>(entities.values());
        for(Entity entity : entityList)
            entity.render();
        
        GL11.glPopMatrix();
        
        gui.render();

        GL11.glPushMatrix();
        int textCount = chatTexts.size();
        GL11.glTranslatef(0.0f, 0.025f * textCount, 0.0f);
        for(int i = 0; i < textCount; i++)
        {
            chatTexts.get(i).render();
            GL11.glTranslatef(0.0f, -0.025f, 0.0f);
        }
        GL11.glPopMatrix();
    }
}
