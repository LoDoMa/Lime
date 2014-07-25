package net.lodoma.lime.gui;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public abstract class Button implements GUIElement
{
    protected Rectangle bounds;
    
    protected boolean mouseHover;
    protected boolean mouseDown;
    protected boolean mouseClick;
    
    public Button(Rectangle bounds)
    {
        this.bounds = bounds;
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
        mouseDown = Input.getMouseDown(Input.LEFT_MOUSE_BUTTON);
        mouseClick = mouseHover && mouseDown;
    }
    
    @Override
    public void render()
    {
        
    }
}
