package net.lodoma.lime.gui.exp;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public class GAbstractButton extends GComponent
{
    private boolean lastHover;
    
    @Override
    public void update()
    {
        super.update();
        
        Vector2 mousePosition = Input.getMousePosition();
        GShape shape = getShape();
        
        boolean hover = shape.isPointInside(mousePosition);
        
        if(hover)
        {
            if(!lastHover) onMouseEnter();
            
            for(int i = 0; i < Input.SIZE_MOUSE; i++)
                     if(Input.getMouseDown(i)) onPressed(i);
                else if(Input.getMouseUp(i))   onReleased(i);
        }
        else if(lastHover) onMouseExit();
        
        lastHover = hover;
    }
    
    protected void onMouseEnter()
    {
        getActionListener().onButtonMouseEnter(this);
    }
    
    protected void onMouseExit()
    {
        getActionListener().onButtonMouseExit(this);
    }
    
    protected void onPressed(int button)
    {
        getActionListener().onButtonPressed(this, button);
    }
    
    protected void onReleased(int button)
    {
        getActionListener().onButtonReleased(this, button);
    }
}
