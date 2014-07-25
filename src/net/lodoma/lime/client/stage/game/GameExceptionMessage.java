package net.lodoma.lime.client.stage.game;

import java.awt.Font;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.menu.MenuButton;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.util.TrueTypeFont;

public class GameExceptionMessage extends Stage
{
    private Exception exception;
    private GUIContainer container;
    
    public GameExceptionMessage(StageManager manager, Exception e)
    {
        super(manager);
        this.exception = e;
        this.container = new GUIContainer();
    }
    
    @Override
    public void onStart()
    {
        container.removeAll();
        container.addElement(new Text(0.5f, 0.6f, 0.025f, 0.025f, exception.getMessage(), new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_CENTER));
        container.addElement(new MenuButton(new Rectangle(0.3f, 0.2f, 0.4f, 0.05f), "Back", new Runnable()
        {
            @Override
            public void run()
            {
                endStage();
            }
        }));
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
