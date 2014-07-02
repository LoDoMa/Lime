package net.lodoma.lime.world.entity;

import net.lodoma.lime.util.Vector2;

public abstract class BodyJoint
{
    protected Entity entity;
    
    protected BodySegment segA;
    protected BodySegment segB;

    protected Vector2 anchorA;
    protected Vector2 anchorB;
    
    public BodyJoint(BodySegment segA, BodySegment segB, Vector2 anchorA, Vector2 anchorB)
    {
        this.segA = segA;
        this.segB = segB;
        this.anchorA = anchorA;
        this.anchorB = anchorB;
    }
    
    public final void setEntity(Entity entity)
    {
        this.entity = entity;
        construct();
    }
    
    protected abstract void construct();
    protected abstract void destroy();
}
