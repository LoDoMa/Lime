package net.lodoma.lime.gui;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

import static org.lwjgl.opengl.GL11.*;

public class Button implements GUIElement
{
    private Rectangle bounds;
    private ButtonListener listener;
    private ButtonRenderer renderer;

    private boolean mouseHover;
    private boolean mouseClick;
    
    public Button(Rectangle bounds, ButtonListener listener, ButtonRenderer renderer)
    {
        this.bounds = bounds;
        this.listener = listener;
        this.renderer = renderer;
    }
    
    public Rectangle getBounds()
    {
        return bounds;
    }
    
    public ButtonListener getListener()
    {
        return listener;
    }
    
    public void setListener(ButtonListener listener)
    {
        this.listener = listener;
    }
    
    public ButtonRenderer getRenderer()
    {
        return renderer;
    }
    
    public void setRenderer(ButtonRenderer renderer)
    {
        this.renderer = renderer;
    }
    
    public boolean isMouseHovering()
    {
        return mouseHover;
    }
    
    public boolean isMouseClicked()
    {
        return mouseClick;
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
        mouseHover = bounds.isInside(mousePosition.x, mousePosition.y);
        boolean mouseUp = Input.getMouseUp(Input.LEFT_MOUSE_BUTTON);
        mouseClick = mouseHover && mouseUp;

        if(listener != null)
            if(mouseHover)
            {
                listener.onHover(this, mousePosition);
                if(mouseClick)
                    listener.onClick(this, mousePosition);
            }
    }
    
    @Override
    public void render()
    {
        if(renderer != null)
        {
            glPushMatrix();
            glTranslatef(bounds.x, bounds.y, 0.0f);
            renderer.render(this);
            glPopMatrix();
        }
    }
}
