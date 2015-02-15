package net.lodoma.lime.gui;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public class UIClickable extends UIObject
{
    public boolean mouseHovering;
    
    @Override
    public void update(double timeDelta)
    {
        Vector2 pos = Input.getMousePosition();
        
        boolean currentHover = false;

        /* NOTE: We must clone the position because we getDimensions right after.
                 It might be possible to avoid that. */
        
        Vector2 position = getPosition().clone();
        Vector2 dimensions = getDimensions();
        if (pos.x >= position.x && pos.x <= position.x + dimensions.x &&
            pos.y >= position.y && pos.y <= position.y + dimensions.y)
            currentHover = true;
        
        if (mouseHovering != currentHover)
        {
            mouseHovering = currentHover;
            onMouseHover(mouseHovering);
        }
        
        if (mouseHovering)
            for (int button = Input.MOUSE_BUTTON_1; button <= Input.MOUSE_BUTTON_8; button++)
                if (Input.getMouseDown(button)) onMousePress(button, true);
                else if (Input.getMouseUp(button)) onMousePress(button, false);
        
        super.update(timeDelta);
    }
    
    public void onMouseHover(boolean state) {}
    public void onMousePress(int button, boolean state) {}
}
