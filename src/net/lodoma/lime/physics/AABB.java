package net.lodoma.lime.physics;

import net.lodoma.lime.util.Transform;
import net.lodoma.lime.util.Vector2;

public class AABB implements Collider
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
    
    public IntersectData collide(Transform thisTransform, Collider other, Transform otherTransform)
    {
        if(other instanceof AABB) return collideAABB(thisTransform, (AABB) other, otherTransform);
        
        throw new IllegalArgumentException();
    }
    
    private IntersectData collideAABB(Transform thisTransform, AABB other, Transform otherTransform)
    {
        Vector2 minExtents = this.minExtents.add(thisTransform.getPosition());
        Vector2 maxExtents = this.maxExtents.add(thisTransform.getPosition());
        
        Vector2 otherMinExtents = other.getMinExtents().add(otherTransform.getPosition());
        Vector2 otherMaxExtents = other.getMaxExtents().add(otherTransform.getPosition());
        
        Vector2 distance1 = otherMinExtents.sub(maxExtents);
        Vector2 distance2 = minExtents.sub(otherMaxExtents);
        
        Vector2 distance = Vector2.max(distance1, distance2);
        float maximumDistance = distance.maxComponent();
        
        return new IntersectData(maximumDistance < 0.0f, distance);
    }
    
    @Override
    public Collider clone()
    {
        return new AABB(new Vector2(minExtents), new Vector2(maxExtents));
    }
}
