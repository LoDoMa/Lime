package net.lodoma.lime.client.stage.game;

import java.awt.Font;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.menu.MenuButton;
import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonListener;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class GameMessage extends Stage
{
    private class BackListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            endStage();
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private String text;
    private GUIContainer container;
    
    public GameMessage(StageManager manager, String text)
    {
        super(manager);
        this.text = text;
        this.container = new GUIContainer();
    }
    
    @Override
    public void preStart()
    {
        
    }
    
    @Override
    public void onStart()
    {
        container.removeAll();
        
        container.addElement(new Text(0.5f, 0.6f, 0.03f, 0.0325f, text, new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_CENTER));
        container.addElement(new MenuButton(new Rectangle(0.3f, 0.2f, 0.4f, 0.05f), new BackListener(), "Back"));
    }
    
    @Override
    public void onEnd()
    {
        
    }
    
    @Override
    public void postEnd()
    {
        
    }
    
    @Override
    public void update(double timeDelta)
    {
        container.update(timeDelta);
        Input.update();
    }
    
    @Override
    public void render()
    {
        container.render();
    }
}
