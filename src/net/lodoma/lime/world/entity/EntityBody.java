package net.lodoma.lime.world.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityBody
{
    private Entity entity;
    private List<BodySegment> segments;
    
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
}
