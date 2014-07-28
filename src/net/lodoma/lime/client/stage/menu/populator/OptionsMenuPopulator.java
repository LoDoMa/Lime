package net.lodoma.lime.client.stage.menu.populator;

import java.awt.Font;

import net.lodoma.lime.client.stage.menu.Menu;
import net.lodoma.lime.client.stage.menu.MenuButton;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.client.window.WindowException;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.gui.Toggle;
import net.lodoma.lime.gui.ToggleListener;
import net.lodoma.lime.security.Credentials;
import net.lodoma.lime.util.TrueTypeFont;

public class OptionsMenuPopulator implements MenuPopulator
{
    private Credentials credentials;
    
    public OptionsMenuPopulator(Credentials credentials)
    {
        this.credentials = credentials;
    }
    
    @Override
    public void populate(final Menu toPopulate)
    {
        GUIContainer container = toPopulate.getContainer();
        
        container.removeAll();
         
        final MenuButton fullscreenToggleButton = new MenuButton(new Rectangle(0.05f, 0.50f, 0.4f, 0.05f), "", null);
        if(Window.isFullscreen()) fullscreenToggleButton.setText("Fullscreen");
        else fullscreenToggleButton.setText("Windowed");
        
        fullscreenToggleButton.setListener(new Runnable()
        {
            private MenuButton fullscreenToggle = fullscreenToggleButton;
            
            @Override
            public void run()
            {
                Window.setFullscreen(!Window.isFullscreen());
                if(Window.isFullscreen()) fullscreenToggle.setText("Fullscreen");
                else fullscreenToggle.setText("Windowed");
                
                try
                {
                    Window.apply();
                }
                catch(WindowException e)
                {
                    e.printStackTrace();
                }
            }
        });
        container.addElement(fullscreenToggleButton);
        
        container.addElement(new Toggle(new Text(0.05f, 0.445f, 0.05f * 0.6f, 0.05f * 0.7f, null, new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_LEFT),
                "vsync on", "vsync off", Window.isVSyncEnabled(),
                new MenuButton(new Rectangle(0.25f, 0.44f, 0.1f, 0.05f), "on", null),
                new MenuButton(new Rectangle(0.35f, 0.44f, 0.1f, 0.05f), "off", null),
                new ToggleListener()
                {
                    @Override
                    public void onToggle(boolean newState)
                    {
                        Window.setVSyncEnabled(newState);
                    }
                }));
        
        container.addElement(new Toggle(new Text(0.05f, 0.385f, 0.05f * 0.6f, 0.05f * 0.7f, null, new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_LEFT),
                "debug on", "debug off", Window.isDebugEnabled(),
                new MenuButton(new Rectangle(0.25f, 0.38f, 0.1f, 0.05f), "on", null),
                new MenuButton(new Rectangle(0.35f, 0.38f, 0.1f, 0.05f), "off", null),
                new ToggleListener()
                {
                    public void onToggle(boolean newState)
                    {
                        Window.setDebugEnabled(newState);
                    };
                }));
        
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.26f, 0.4f, 0.05f), "Back", new Runnable()
        {
            private Menu menu = toPopulate;
            
            @Override
            public void run()
            {
                menu.setPopulator(new MainMenuPopulator(credentials));
            }
        }));
    }
}
