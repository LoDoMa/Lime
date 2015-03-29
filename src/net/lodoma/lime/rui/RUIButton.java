package net.lodoma.lime.rui;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public class RUIButton extends RUILabel
{
    protected boolean hover;
    
    public RUIButton(RUIElement parent)
    {
        super(parent);
        values.set("default", "font-size", RUIValue.SIZE_1);
    }
    
    protected void checkClick()
    {
        if (eventListener != null)
        {
            if (Input.getMouseDown(Input.MOUSE_BUTTON_LEFT))
                eventListener.onEvent(RUIEventType.MOUSE_PRESS, null);
            else if (Input.getMouseUp(Input.MOUSE_BUTTON_LEFT))
                eventListener.onEvent(RUIEventType.MOUSE_RELEASE, null);
        }
    }
    
    @Override
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            super.update(timeDelta);

            Vector2 mousePos = Input.getMousePosition();
            
            boolean prevhover = hover;
            hover = false;
            if (mousePos.x >= position_c.x && mousePos.y >= position_c.y)
                if (mousePos.x - position_c.x <= dimensions_c.x && mousePos.y - position_c.y <= dimensions_c.y)
                    hover = true;

            if (eventListener != null)
            {
                if (hover && !prevhover) eventListener.onEvent(RUIEventType.MOUSE_HOVER_ON, null);
                else if (!hover && prevhover) eventListener.onEvent(RUIEventType.MOUSE_HOVER_OFF, null);
            }
            
            if (hover)
                checkClick();
            
            if (hover) state = "hover";
            else state = "default";
        }
    }
}
