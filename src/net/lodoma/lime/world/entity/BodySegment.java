package net.lodoma.lime.world.entity;

public abstract class BodySegment
{
    protected Entity entity;
    
    public final void setEntity(Entity entity)
    {
        this.entity = entity;
    }
}
