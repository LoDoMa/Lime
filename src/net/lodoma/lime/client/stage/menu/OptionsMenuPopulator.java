package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.client.window.WindowException;
import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonListener;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.SliderListener;
import net.lodoma.lime.gui.Toggle;
import net.lodoma.lime.gui.ToggleListener;
import net.lodoma.lime.gui.simple.SimpleButton;
import net.lodoma.lime.gui.simple.SimpleSlider;
import net.lodoma.lime.gui.simple.SimpleToggle;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.Vector2;

public class OptionsMenuPopulator implements MenuPopulator
{
    private class FullscreenListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            Window.fullscreen = !Window.fullscreen;
            ((SimpleButton) button).setText(Window.fullscreen ? "Fullscreen" : "Windowed");
            
            try
            {
                Window.recreate();
            }
            catch(WindowException e)
            {
                e.printStackTrace();
            }
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private class VSyncListener implements ToggleListener
    {
        @Override
        public void onToggle(Toggle toggle, boolean newState)
        {
            Window.vsync = newState;
        }
    }
    
    private class DebugListener implements ToggleListener
    {
        @Override
        public void onToggle(Toggle toggle, boolean newState)
        {
            Window.debugEnabled = newState;
        }
    }
    
    private class SoundListener implements SliderListener
    {
        @Override
        public void onMove(float value)
        {
            System.out.println("Sound level: " + (int) (value * 100) + "%");
        }
    }
    
    private class BackListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            menu.setPopulator(new MainMenuPopulator(credentials));
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private Credentials credentials;
    private Menu menu;
    
    public OptionsMenuPopulator(Credentials credentials)
    {
        this.credentials = credentials;
    }
    
    @Override
    public void populate(final Menu toPopulate)
    {
        menu = toPopulate;
        
        GUIContainer container = toPopulate.getContainer();
        
        container.removeAll();
         
        container.addElement(new SimpleButton(new Rectangle(0.05f, 0.50f, 0.4f, 0.05f), new FullscreenListener(), Window.fullscreen ? "Fullscreen" : "Windowed"));
        container.addElement(new SimpleToggle(new Rectangle(0.05f, 0.44f, 0.4f, 0.05f), Window.vsync, "VSync:", "on", "off", new VSyncListener()));
        container.addElement(new SimpleToggle(new Rectangle(0.05f, 0.38f, 0.4f, 0.05f), Window.debugEnabled, "Debug:", "on", "off", new DebugListener()));
        container.addElement(new SimpleSlider(new Rectangle(0.55f, 0.44f, 0.4f, 0.05f), 0.5f, "Sound:", new SoundListener()));
        container.addElement(new SimpleButton(new Rectangle(0.3f, 0.26f, 0.4f, 0.05f), new BackListener(), "Back"));
    }
}
