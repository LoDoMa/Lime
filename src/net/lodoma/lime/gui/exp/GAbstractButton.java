package net.lodoma.lime.gui.exp;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public class GAbstractButton extends GComponent
{
    @Override
    public void update()
    {
        super.update();
        
        Vector2 mousePosition = Input.getMousePosition();
        GShape shape = getShape();
        
        boolean hover = shape.isPointInside(mousePosition);
        
        if(hover)
        {
            onHovering();
            
            int buttonc = Input.getMouseButtonCount();
            for(int i = 0; i < buttonc; i++)
                     if(Input.getMouseDown(i)) onPressed(i);
                else if(Input.getMouseUp(i))   onReleased(i);
        }
    }
    
    protected void onHovering()
    {
        actionListener.onButtonHovering(this);
    }
    
    protected void onPressed(int button)
    {
        actionListener.onButtonPressed(this, button);
    }
    
    protected void onReleased(int button)
    {
        actionListener.onButtonReleased(this, button);
    }
}
