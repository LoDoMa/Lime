package net.lodoma.lime.gui;

import net.lodoma.lime.util.Vector2;

public interface ButtonListener
{
    public void onClick(Button button, Vector2 mousePosition);
    public void onHover(Button button, Vector2 mousePosition);
}
