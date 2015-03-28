package net.lodoma.lime.rui;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public class RUIButton extends RUIElement
{
    private final RUILabel label;
    private boolean hover;
    
    public RUIButton(RUIElement parent)
    {
        super(parent);
        
        label = new RUILabel(this);
        label.values.set("default", "font-size", RUIValue.SIZE_1);
        
        addChild("$LABEL", label);
    }
    
    @Override
    public void loadData(RUIParserData data)
    {
        synchronized (treeLock)
        {
            super.loadData(data);
            
            data.copy("text", RUIValueType.STRING, label.values);
            data.copy("font-name", RUIValueType.STRING, label.values);
            data.copy("font-size", RUIValueType.SIZE, label.values);
            data.copy("horizontal-alignment", RUIValueType.STRING, label.values);
            data.copy("vertical-alignment", RUIValueType.STRING, label.values);
        }
    }
    
    @Override
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            super.update(timeDelta);
            
            boolean prevhover = hover;
            Vector2 mousePos = Input.getMousePosition();
            
            hover = false;
            if (mousePos.x >= position_c.x && mousePos.y >= position_c.y)
                if (mousePos.x - position_c.x <= dimensions_c.x && mousePos.y - position_c.y <= dimensions_c.y)
                    hover = true;
            
            if (eventListener != null)
            {
                if (hover && !prevhover) eventListener.onEvent(RUIEventType.MOUSE_HOVER_ON, null);
                else if (!hover && prevhover) eventListener.onEvent(RUIEventType.MOUSE_HOVER_OFF, null);
                
                if (hover)
                {
                    if (Input.getMouseDown(Input.MOUSE_BUTTON_LEFT))
                        eventListener.onEvent(RUIEventType.MOUSE_PRESS, null);
                    else if (Input.getMouseUp(Input.MOUSE_BUTTON_LEFT))
                        eventListener.onEvent(RUIEventType.MOUSE_RELEASE, null);
                }
            }
            
            if (hover) state = "hover";
            else state = "default";

            label.values.set(label.state, "visible", values.get(state, "visible"));
            label.values.set(label.state, "foreground-color", values.get(state, "foreground-color"));
        }
    }
}
