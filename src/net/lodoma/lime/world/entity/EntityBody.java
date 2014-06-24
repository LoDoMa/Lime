package net.lodoma.lime.world.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityBody
{
    private List<BodySegment> segments;
    
    public EntityBody()
    {
        segments = new ArrayList<BodySegment>();
    }
}
