package net.lodoma.lime.gui;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public class Slider implements GUIElement
{
    private Rectangle bounds;
    private float value;
    private SliderListener listener;
    private boolean hover;
    
    public Slider(Rectangle bounds, float value, SliderListener listener)
    {
        this.bounds = bounds;
        this.value = value;
        this.listener = listener;
    }
    
    public Rectangle getBounds()
    {
        return bounds;
    }
    
    public float getValue()
    {
        return value;
    }
    
    public SliderListener getListener()
    {
        return listener;
    }
    
    public void setListener(SliderListener listener)
    {
        this.listener = listener;
    }
    
    public boolean isMouseHovering()
    {
        return hover;
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
        hover = bounds.isInside(mousePosition.x, mousePosition.y);
        if(listener != null)
            if(hover)
                if(Input.getMouse(Input.LEFT_MOUSE_BUTTON))
                {
                    float newValue = (mousePosition.x - bounds.x) / bounds.w;
                    if(value != newValue)
                    {
                        value = newValue;
                        listener.onMove(value);
                    }
                }
    }
    
    @Override
    public void render()
    {
        
    }
}
