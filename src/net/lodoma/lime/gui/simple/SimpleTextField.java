package net.lodoma.lime.gui.simple;

import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.TextField;
import net.lodoma.lime.util.Vector2;

public class SimpleTextField extends TextField
{
    private SimpleButtonRenderer renderer;
    private float transparency;
    
    public SimpleTextField(Rectangle bounds, String text)
    {
        super(bounds, new SimpleText(0.0f, 0.0f, bounds.w, bounds.h, text));

        renderer = new SimpleButtonRenderer();
        setRenderer(renderer);
        transparency = 0.0f;
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        super.update(timeDelta, mousePosition);
        
        if(isMouseHovering() || isActive())
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
        
        renderer.setTransparency(transparency);
    }
}
