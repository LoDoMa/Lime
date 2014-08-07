package net.lodoma.lime.gui.exp;

public abstract class GActionListener
{
    public void onButtonMouseEnter(GAbstractButton gbutton) {}
    public void onButtonMouseExit(GAbstractButton gbutton) {}
    public void onButtonPressed(GAbstractButton gbutton, int mouseButton) {}
    public void onButtonReleased(GAbstractButton gbutton, int mouseButton) {}
}
