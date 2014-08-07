package net.lodoma.lime.gui.exp;

public class GNullShape extends GShape
{
    public static final GNullShape INSTANCE = new GNullShape();
    
    @Override
    public boolean isPointInside(float x, float y) { return false; }
    
    @Override
    public void render() {}
}
