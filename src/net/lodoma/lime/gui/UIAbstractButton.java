package net.lodoma.lime.gui;

import net.lodoma.lime.input.Input;

public class UIAbstractButton extends UIClickable
{
    public UICallback onClick;
    
    public UIAbstractButton(UICallback onClick)
    {
        this.onClick = onClick;
    }
    
    @Override
    public void onMousePress(int button, boolean state)
    {
        if (button == Input.MOUSE_BUTTON_LEFT && state == false && onClick != null)
            onClick.call();
    }
}
