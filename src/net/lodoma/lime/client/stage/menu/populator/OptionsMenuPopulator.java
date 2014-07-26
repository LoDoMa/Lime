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
        
        String choices[] = new String[WindowResolutionOptions.displayModes.size()];
        Integer[] keys = new Integer[WindowResolutionOptions.displayModes.size()];
        WindowResolutionOptions.displayModes.keySet().toArray(keys);
        
        for(int i = 0; i < keys.length; i++)
        {
            int key = keys[i];
            int w = (key >> 16) & 0xFFFF;
            int h = key & 0xFFFF;
            choices[i] = w + ":" + h;
        }
        
        final MenuButton setButton = new MenuButton(new Rectangle(0.10f, 0.44f, 0.30f, 0.05f), "", null);
        container.addElement(new Choice(
                new MenuButton(new Rectangle(0.05f, 0.44f, 0.05f, 0.05f), "<", null),
                setButton,
                new MenuButton(new Rectangle(0.40f, 0.44f, 0.05f, 0.05f), ">", null),
                choices,
                new ChoiceListener()
                {
                    @Override
                    public void onSet(String[] choices, int current)
                    {
                        String[] cmp = choices[current].split(":");
                        int w = Integer.parseInt(cmp[0]);
                        int h = Integer.parseInt(cmp[1]);
                        Window.setResolution(w, h);
                        
                        Window.apply();
                        Window.setupGL();
                    }
                    
                    @Override
                    public void onChange(String[] choices, int current)
                    {
                        setButton.setText(choices[current]);
                    }
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
