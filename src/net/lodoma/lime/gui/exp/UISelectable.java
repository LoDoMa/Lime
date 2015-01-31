package net.lodoma.lime.gui.exp;

import net.lodoma.lime.input.Input;

public class UISelectable extends UIClickable
{
    public boolean selected = false;
    
    @Override
    public void update(double timeDelta)
    {
        super.update(timeDelta);

        for (int button = Input.MOUSE_BUTTON_1; button <= Input.MOUSE_BUTTON_8; button++)
            if (Input.getMouseDown(button))
                if (mouseHovering)
                {
                    if (!selected) selected = onSelect(button);
                }
                else
                {
                    if (selected) selected = !onDeselect(button);
                }
    }
    
    public boolean onSelect(int button) { return false; }
    
    public boolean onDeselect(int button) { return false; }
}
