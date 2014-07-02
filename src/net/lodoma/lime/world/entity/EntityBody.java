package net.lodoma.lime.world.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityBody
{
    private Entity entity;
    private List<BodySegment> segments;
    private List<BodyJoint> joints;
    
    public EntityBody(Entity entity)
    {
        this.entity = entity;
        segments = new ArrayList<BodySegment>();
    }
    
    public void addBodySegment(BodySegment segment)
    {
        segments.add(segment);
        segment.setEntity(entity);
    }
    
    public void addBodyJoint(BodyJoint joint)
    {
        joints.add(joint);
        joint.setEntity(entity);
    }
}
