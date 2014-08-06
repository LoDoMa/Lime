package net.lodoma.lime.gui.simple;

import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonListener;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.util.Vector2;

public class SimpleButton extends Button
{
    private SimpleText text;
    
    private SimpleButtonRenderer renderer;
    private float transparency;
    
    public SimpleButton(Rectangle bounds, ButtonListener listener, String text)
    {
        super(bounds, listener, null);
        
        this.text = new SimpleText(0.0f, 0.0f, bounds.w, bounds.h, text);
        
        renderer = new SimpleButtonRenderer();
        setRenderer(renderer);
        transparency = 0.0f;
    }
    
    public void setText(String text)
    {
        this.text.setText(text);
    }
    
    @Override
    public void create()
    {
        super.create();
        text.create();
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        text.destroy();
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        super.update(timeDelta, mousePosition);
        
        if(isMouseHovering())
        {
            if(transparency < 1.0f)
            {
                transparency += timeDelta * 5.0f;
                if(transparency > 1.0f)
                    transparency = 1.0f;
            }
        }
        else
            transparency = 0.0f;
        
        renderer.setText(text);
        renderer.setTransparency(transparency);
    }
}
