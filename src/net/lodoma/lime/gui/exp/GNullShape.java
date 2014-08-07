package net.lodoma.lime.gui.exp;

public class GNullShape extends GShape
{
    @Override
    public boolean isPointInside(float x, float y) { return false; }
    
    @Override
    public void render() {}
}
