package net.lodoma.lime.gui;

public interface ChoiceListener
{
    public void onChange(String[] choices, int current);
    public void onSet(String[] choices, int current);
}
