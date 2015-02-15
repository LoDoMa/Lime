package net.lodoma.lime.gui.layout;

import net.lodoma.lime.gui.UILayout;
import net.lodoma.lime.gui.UIObject;

public class UIVerticalFlowLayout extends UILayout
{
    private final float offset;
    private float total;
    
    public UIVerticalFlowLayout(float offset)
    {
        this.offset = offset;
    }
    
    @Override
    protected void rebuild()
    {
        super.rebuild();
        
        total = 0.0f;
        object.foreachChild((UIObject child, Integer index) -> {
            child.getLocalPosition().set(0.0f, total);
            float height = child.getLocalDimensions().y;
            
            if (offset < 0) total -= height;
            else total += height;
            
            total += offset;
            return false;
        });
    }
}
