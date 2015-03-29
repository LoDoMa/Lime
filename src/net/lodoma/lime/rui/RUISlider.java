package net.lodoma.lime.rui;

import net.lodoma.lime.input.Input;

public class RUISlider extends RUIProgressBar
{
    public RUISlider(RUIElement parent)
    {
        super(parent);
    }
    
    @Override
    protected void checkClick()
    {
        super.checkClick();
        
        if (Input.getMouse(Input.MOUSE_BUTTON_LEFT))
        {
            float newProgress = Math.round((Input.getMousePosition().x - position_c.x) / dimensions_c.x * 100.0f) / 100.0f;
            float oldProgress = values.get("default", "progress").toSize();
            if (newProgress != oldProgress)
            {
                values.set("default", "progress", new RUIValue(newProgress));
                if (eventListener != null)
                    eventListener.onEvent(RUIEventType.SLIDER_VALUE_CHANGE, null);
            }
        }
    }
}
