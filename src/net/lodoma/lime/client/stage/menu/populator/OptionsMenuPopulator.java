package net.lodoma.lime.client.stage.menu.populator;

import net.lodoma.lime.client.stage.menu.Menu;
import net.lodoma.lime.client.stage.menu.MenuButton;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.client.window.WindowResolutionOptions;
import net.lodoma.lime.gui.Choice;
import net.lodoma.lime.gui.ChoiceListener;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.security.Credentials;

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
                
                Window.apply();
            }
        });
        container.addElement(fullscreenToggleButton);
        
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
