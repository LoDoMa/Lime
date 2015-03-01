package net.lodoma.lime.gui.editor.layout;

import net.lodoma.lime.gui.UILayout;
import net.lodoma.lime.gui.UIObject;
import net.lodoma.lime.util.Vector2;

public class EditorPanelHorizontalLayout extends UILayout
{
    private final Vector2 offset = new Vector2();
    private float total;
    
    public EditorPanelHorizontalLayout(Vector2 offset)
    {
        this.offset.set(offset);
    }
    
    @Override
    protected void rebuild()
    {
        super.rebuild();
        
        Vector2 objectDimensions = object.getDimensions();
        float targetHeight = objectDimensions.y - offset.y * 2;
        
        total = offset.x;
        if (total < 0)
            total += objectDimensions.x;
        
        object.foreachChild((UIObject child, Integer index) -> {
            Vector2 dimensions = child.getLocalDimensions();
            float ratio = dimensions.x / dimensions.y;
            dimensions.set(targetHeight * ratio, targetHeight);
            
            Vector2 position = child.getLocalPosition();
            position.set(total, offset.y);
            
            total += offset.x;
            if (offset.x < 0)
                total -= dimensions.x;
            else
                total += dimensions.x;
            
            return false;
        });
    }
}
