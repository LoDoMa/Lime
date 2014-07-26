package net.lodoma.lime.gui;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public abstract class Button implements GUIElement
{
    protected Rectangle bounds;
    
    protected boolean mouseHover;
    protected boolean mouseUp;
    protected boolean mouseClick;
    
    private Runnable listener;
    
    public Button(Rectangle bounds)
    {
        this.bounds = bounds;
    }
    
    public void setListener(Runnable listener)
    {
        this.listener = listener;
    }
    
    @Override
    public void create()
    {
        
    }
    
    @Override
    public void destroy()
    {
        
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        mouseHover = bounds.inside(mousePosition.x, mousePosition.y);
        mouseUp = Input.getMouseUp(Input.LEFT_MOUSE_BUTTON);
        mouseClick = mouseHover && mouseUp;

        if(listener != null)
            if(mouseHover)
                if(mouseClick)
                    listener.run();
    }
    
    @Override
    public void render()
    {
        
    }
}
