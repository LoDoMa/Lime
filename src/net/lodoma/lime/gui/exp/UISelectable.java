package net.lodoma.lime.gui.exp;

import net.lodoma.lime.input.Input;

public class UISelectable extends UIClickable
{
    public boolean selected;
    
    @Override
    public void update(double timeDelta)
    {
        super.update(timeDelta);

        for (int button = Input.MOUSE_BUTTON_1; button <= Input.MOUSE_BUTTON_8; button++)
            if (Input.getMouseDown(button))
                if (mouseHovering)
                {
                    if (!selected) selected = isSelect(button);
                }
                else
                {
                    if (selected) selected = !isDeselect(button);
                }
    }
    
    public boolean isSelect(int button) { return false; }
    
    public boolean isDeselect(int button) { return false; }
}
