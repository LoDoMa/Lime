package net.lodoma.lime.client.stage.menu.populator;

import net.lodoma.lime.client.stage.menu.Menu;
import net.lodoma.lime.client.stage.menu.MenuButton;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;

public class MultiplayerMenuPopulator implements MenuPopulator
{
    @Override
    public void populate(Menu toPopulate)
    {
        GUIContainer container = toPopulate.getContainer();
        
        container.removeAll();
        
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.05f, 0.4f, 0.05f), "Back", new Runnable()
        {
            private Menu menu = toPopulate;
            
            @Override
            public void run()
            {
                menu.setPopulator(new MainMenuPopulator());
            }
        }));
    }
}
