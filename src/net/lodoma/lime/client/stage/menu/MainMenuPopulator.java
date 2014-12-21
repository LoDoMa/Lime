package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonListener;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.simple.SimpleButton;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.Vector2;

public class MainMenuPopulator implements MenuPopulator
{
    private class MultiplayerListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            menu.setPopulator(new MultiplayerMenuPopulator(credentials));
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private class OptionsListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            menu.setPopulator(new OptionsMenuPopulator(credentials));
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private class ExitListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            Window.requestClose();
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private Credentials credentials;
    private Menu menu;
    
    public MainMenuPopulator(Credentials credentials)
    {
        this.credentials = credentials;
    }
    
    @Override
    public void populate(Menu toPopulate)
    {
        menu = toPopulate;
        
        GUIContainer container = toPopulate.getContainer();
        
        container.removeAll();
        
        container.addElement(new SimpleButton(new Rectangle(0.05f, 0.50f, 0.4f, 0.05f), null, "Campaign"));
        container.addElement(new SimpleButton(new Rectangle(0.05f, 0.44f, 0.4f, 0.05f), new MultiplayerListener(), "Multiplayer"));
        container.addElement(new SimpleButton(new Rectangle(0.05f, 0.38f, 0.4f, 0.05f), null, "Arcade"));
        container.addElement(new SimpleButton(new Rectangle(0.05f, 0.32f, 0.4f, 0.05f), new OptionsListener(), "Options"));
        container.addElement(new SimpleButton(new Rectangle(0.05f, 0.26f, 0.4f, 0.05f), new ExitListener(), "Exit"));
    }
}
