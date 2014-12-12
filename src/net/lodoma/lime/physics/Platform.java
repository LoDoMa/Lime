package net.lodoma.lime.physics;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

public class Platform implements Identifiable<Integer>
{
    public int identifier;
    public Vector2 pointA;
    public Vector2 pointB;
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
}
