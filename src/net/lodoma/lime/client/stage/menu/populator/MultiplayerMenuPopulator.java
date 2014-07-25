package net.lodoma.lime.client.stage.menu.populator;

import net.lodoma.lime.client.stage.game.Game;
import net.lodoma.lime.client.stage.menu.Menu;
import net.lodoma.lime.client.stage.menu.MenuButton;
import net.lodoma.lime.client.stage.menu.MenuTextField;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;

public class MultiplayerMenuPopulator implements MenuPopulator
{
    @Override
    public void populate(final Menu toPopulate)
    {
        GUIContainer container = toPopulate.getContainer();
        
        container.removeAll();
        
        final MenuTextField host = new MenuTextField(new Rectangle(0.05f, 0.5f, 0.4f, 0.05f), "localhost");
        container.addElement(host);
        
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.44f, 0.4f, 0.05f), "join", new Runnable()
        {
            private Menu menu = toPopulate;
            private MenuTextField hostTextField = host;
            
            @Override
            public void run()
            {
                new Game(menu.getManager(), hostTextField.getText()).startStage();
            }
        }));
        
        container.addElement(new MenuButton(new Rectangle(0.05f, 0.26f, 0.4f, 0.05f), "Back", new Runnable()
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
