package net.lodoma.lime.client.stage.menu.populator;

import net.lodoma.lime.client.stage.menu.Menu;
import net.lodoma.lime.client.stage.menu.MenuButton;
import net.lodoma.lime.client.stage.menu.MenuTextField;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;

public class MainMenuPopulator implements MenuPopulator
{
    @Override
    public void populate(final Menu toPopulate)
    {
        GUIContainer container = toPopulate.getContainer();
        
        container.removeAll();
        
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.50f, 0.4f, 0.05f), "Campaign", null));
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.44f, 0.4f, 0.05f), "Multiplayer", new Runnable()
        {
            private Menu menu = toPopulate;
            
            @Override
            public void run()
            {
                menu.setPopulator(new MultiplayerMenuPopulator());
            }
        }));
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.38f, 0.4f, 0.05f), "Arcade", null));
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.32f, 0.4f, 0.05f), "Options", null));
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.26f, 0.4f, 0.05f), "Exit", new Runnable()
        {
            @Override
            public void run()
            {
                Window.requestClose();
            }
        }));
        container.addElement(new MenuTextField(new Rectangle(0.05f, 0.60f, 0.4f, 0.05f), "initial"));
    }
}
