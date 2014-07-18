package net.lodoma.lime.physics.entity;

import net.lodoma.lime.world.entity.EntityWorld;

public class LuaEntity
{
    private Entity entity;
    
    public LuaEntity(Entity entity)
    {
        this.entity = entity;
    }
    
    public long getID()
    {
        return entity.ID;
    }
    
    public long getHash()
    {
        return entity.hash;
    }
    
    public EntityWorld getWorld()
    {
        return entity.world;
    }
}
