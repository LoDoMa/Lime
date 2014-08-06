package net.lodoma.lime.client.stage.menu.populator;

import java.awt.Font;

import net.lodoma.lime.client.stage.menu.Menu;
import net.lodoma.lime.client.stage.menu.MenuButton;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.client.window.WindowException;
import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonListener;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.gui.Toggle;
import net.lodoma.lime.gui.ToggleListener;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class OptionsMenuPopulator implements MenuPopulator
{
    private class FullscreenListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            Window.setFullscreen(!Window.isFullscreen());
            ((MenuButton) button).setText(Window.isFullscreen() ? "Fullscreen" : "Windowed");
            
            try
            {
                Window.apply();
            }
            catch(WindowException e)
            {
                e.printStackTrace();
            }
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
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
         
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.50f, 0.4f, 0.05f), new FullscreenListener(), Window.isFullscreen() ? "Fullscreen" : "Windowed"));
        
        container.addElement(new Toggle(new Text(0.05f, 0.445f, 0.05f * 0.6f, 0.05f * 0.7f, null, new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_LEFT),
                Window.isVSyncEnabled(),
                new MenuButton(new Rectangle(0.25f, 0.44f, 0.1f, 0.05f), null, "on"),
                new MenuButton(new Rectangle(0.35f, 0.44f, 0.1f, 0.05f), null, "off"),
                new ToggleListener()
                {
                    @Override
                    public void onToggle(Toggle toggle, boolean newState)
                    {
                        toggle.getText().setText(newState ? "vsync on" : "vsync off");
                        Window.setVSyncEnabled(newState);
                    }
                }));
        
        container.addElement(new Toggle(new Text(0.05f, 0.385f, 0.05f * 0.6f, 0.05f * 0.7f, null, new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_LEFT),
                Window.isDebugEnabled(),
                new MenuButton(new Rectangle(0.25f, 0.38f, 0.1f, 0.05f), null, "on"),
                new MenuButton(new Rectangle(0.35f, 0.38f, 0.1f, 0.05f), null, "off"),
                new ToggleListener()
                {
                    public void onToggle(Toggle toggle, boolean newState)
                    {
                        toggle.getText().setText(newState ? "debug on" : "debug off");
                        Window.setDebugEnabled(newState);
                    };
                }));

        container.addElement(new MenuButton(new Rectangle(0.05f, 0.26f, 0.4f, 0.05f), new BackListener(), "Back"));
    }
}
