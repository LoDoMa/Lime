package net.lodoma.lime.world.client;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.chat.ChatReceiver;
import net.lodoma.lime.chat.ChatSender;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.gui.ActiveTextField;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.SystemHelper;
import net.lodoma.lime.util.TrueTypeFont;

public class ChatConsole implements ChatSender, ChatReceiver
{
    private Client client;
    
    private GUIContainer gui;
    
    private boolean inChat;
    private ActiveTextField chatField;
    private List<ChatManager> chatManagers;
    private List<String> chatToSend;
    
    private List<Text> chatTexts;
    private Map<Text, Double> enterTimes;
    
    public ChatConsole(ClientsideWorld world)
    {
        this.client = world.getClient();
        
        gui = new GUIContainer();
        
        chatManagers = new ArrayList<ChatManager>();
        chatToSend = new ArrayList<String>();
        
        chatTexts = new ArrayList<Text>();
        enterTimes = new HashMap<Text, Double>();
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
