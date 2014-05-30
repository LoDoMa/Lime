package net.lodoma.lime.world.entity;

import java.util.HashSet;
import java.util.Set;

import net.lodoma.lime.util.Vector2;

public abstract class EntitySegment
{
    protected Entity entity;
    
    protected EntitySegment parent;
    protected Vector2 anchor;

    protected Vector2 position;
    protected float angle;
    
    protected Set<EntitySegment> children;
    
    {
        children = new HashSet<EntitySegment>();
    }
    
    public final Entity getEntity()
    {
        return entity;
    }
    
    public final void setEntity(Entity entity)
    {
        this.entity = entity;
    }
    
    public final EntitySegment getParent()
    {
        return parent;
    }
    
    public final void setParent(EntitySegment parent)
    {
        if(this.parent != null)
            this.parent.removeChild(this);
        this.parent = parent;
        this.parent.addChild(this);
    }
    
    public final Vector2 getAnchor()
    {
        return anchor;
    }
    
    public final void setAnchor(Vector2 anchor)
    {
        this.anchor = anchor;
    }
    
    public final Vector2 getPosition()
    {
        return position;
    }

    public final void setPosition(Vector2 position)
    {
        this.position = position;
    }

    public final float getAngle()
    {
        return angle;
    }

    public final void setAngle(float angle)
    {
        this.angle = angle;
    }
    
    public void addChild(EntitySegment child)
    {
        children.add(child);
    }
    
    public void removeChild(EntitySegment child)
    {
        children.remove(child);
    }

    protected void updatePosition()
    {
        float parentAngle;
        Vector2 parentPosition;
        
        if(parent == null)
        {
            parentPosition = entity.getBasePosition();
            parentAngle = entity.getBaseAngle();
        }
        else
        {
            parentPosition = parent.getPosition();
            parentAngle = parent.getAngle();
        }
        
        position.set(parentPosition);
        position.addLocal(anchor);
        position.rotateAroundLocalDeg(parentPosition, parentAngle);
    }
    
    public abstract void update();
    public abstract void render();
}
