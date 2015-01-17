package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.game.Game;
import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonListener;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.simple.SimpleButton;
import net.lodoma.lime.gui.simple.SimpleTextField;
import net.lodoma.lime.util.Vector2;

public class MultiplayerMenuPopulator implements MenuPopulator
{
    private class JoinListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            new Game(menu.getManager(), hostField.getText()).startStage();
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private class BackListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            menu.setPopulator(new MainMenuPopulator());
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private Menu menu;
    
    private SimpleTextField hostField;
    
    public MultiplayerMenuPopulator()
    {
        
    }
    
    @Override
    public void populate(final Menu toPopulate)
    {
        menu = toPopulate;
        
        GUIContainer container = toPopulate.getContainer();
        
        container.removeAll();

        hostField = new SimpleTextField(new Rectangle(0.05f, 0.5f, 0.4f, 0.05f), "localhost");
        
        container.addElement(hostField);
        container.addElement(new SimpleButton(new Rectangle(0.05f, 0.44f, 0.4f, 0.05f), new JoinListener(), "Join"));
        container.addElement(new SimpleButton(new Rectangle(0.05f, 0.26f, 0.4f, 0.05f), new BackListener(), "Back"));
    }
}
