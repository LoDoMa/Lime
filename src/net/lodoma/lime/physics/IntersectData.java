package net.lodoma.lime.physics;

import net.lodoma.lime.util.Vector2;

public class IntersectData
{
    private boolean intersects;
    private Vector2 direction;
    
    public IntersectData(boolean intersects, Vector2 direction)
    {
        this.intersects = intersects; 
        this.direction = direction;
    }
    
    public boolean intersects()
    {
        return intersects;
    }
    
    public Vector2 getDirection()
    {
        return direction;
    }
    
    public float getDistance()
    {
        return direction.magnitudeLocal();
    }
}
