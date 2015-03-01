package net.lodoma.lime.gui.editor;

import net.lodoma.lime.gui.UICallback;
import net.lodoma.lime.gui.UIObject;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

class EditorWindowHandle extends UIObject
{
    private UICallback callback;
    
    private boolean mousePress;
    private int pressID;
    
    public Vector2 lastPress;
    public Vector2 currentPress;
    
    public EditorWindowHandle(Vector2 position, Vector2 dimensions, UICallback callback)
    {
        super();
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        
        this.callback = callback;
    }
    
    @Override
    public void update(double timeDelta)
    {
        currentPress = Input.getMousePosition();
        
        boolean currentHover = false;

        /* NOTE: We must clone the position because we getDimensions right after.
                 It might be possible to avoid that. */
        
        Vector2 position = getPosition().clone();
        Vector2 dimensions = getDimensions();
        if (currentPress.x >= position.x && currentPress.x <= position.x + dimensions.x &&
            currentPress.y >= position.y && currentPress.y <= position.y + dimensions.y)
            currentHover = true;
        
        for (int button = Input.MOUSE_BUTTON_1; button <= Input.MOUSE_BUTTON_8; button++)
            if (Input.getMouseDown(button) && currentHover)
            {
                mousePress = true;
                lastPress = currentPress;
                pressID = button;
            }
            else if (Input.getMouseUp(button) && button == pressID)
                mousePress = false;
        
        if (mousePress)
            callback.call();
        
        lastPress = currentPress;
        
        super.update(timeDelta);
    }
}
