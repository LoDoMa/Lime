package net.lodoma.lime.world.entity;

import net.lodoma.lime.util.Vector2;

public class RevoluteBodyJoint extends BodyJoint
{
    public RevoluteBodyJoint(BodySegment segA, BodySegment segB, Vector2 anchorA, Vector2 anchorB)
    {
        super(segA, segB, anchorA, anchorB);
    }

    @Override
    protected void construct()
    {
        
    }
}
