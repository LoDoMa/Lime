package net.lodoma.lime.client.stage.mainmenu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;

public class MainMenu extends Stage
{
    private GUIContainer container;
    
    public MainMenu(StageManager manager)
    {
        super(manager);
        
        container = new GUIContainer();
    }
    
    @Override
    public void onStart()
    {
        container.removeAll();

        container.addElement(new MainMenuButton(new Rectangle(0.05f, 0.50f, 0.4f, 0.05f), "Campaign", "My type of font"));
        container.addElement(new MainMenuButton(new Rectangle(0.05f, 0.44f, 0.4f, 0.05f), "Multiplayer", "My type of font"));
        container.addElement(new MainMenuButton(new Rectangle(0.05f, 0.38f, 0.4f, 0.05f), "Arcade", "My type of font"));
        container.addElement(new MainMenuButton(new Rectangle(0.05f, 0.32f, 0.4f, 0.05f), "Options", "My type of font"));
        container.addElement(new MainMenuButton(new Rectangle(0.05f, 0.26f, 0.4f, 0.05f), "Exit", "My type of font"));
    }
    
    @Override
    public void onEnd()
    {
        
    }
    
    @Override
    public void update(double timeDelta)
    {
        container.update(timeDelta);
    }
    
    @Override
    public void render()
    {
        container.render();
    }
}
