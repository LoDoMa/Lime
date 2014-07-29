package net.lodoma.lime.actor;

import net.lodoma.lime.physics.entity.Entity;

public class UserActor
{
    private Entity actor;
    
    public UserActor()
    {
        
    }
    
    public Entity getActor()
    {
        return actor;
    }
    
    public void setActor(Entity actor)
    { 
        this.actor = actor;
    }
}
