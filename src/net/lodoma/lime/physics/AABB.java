package net.lodoma.lime.physics;

import net.lodoma.lime.util.Vector2;

public class AABB
{
    private final Vector2 minExtents;
    private final Vector2 maxExtents;
    
    public AABB(Vector2 minExtents, Vector2 maxExtents)
    {
        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
    }
    
    public final Vector2 getMinExtents()
    {
        return minExtents;
    }
    
    public final Vector2 getMaxExtents()
    {
        return maxExtents;
    }
    
    public float intersect(AABB other)
    {
        Vector2 distance1 = other.getMinExtents().sub(maxExtents);
        Vector2 distance2 = minExtents.sub(other.getMaxExtents());
        
        Vector2 distance = Vector2.max(distance1, distance2);
        float maximumDistance = distance.maxComponent();
        
        return maximumDistance;
    }
}
