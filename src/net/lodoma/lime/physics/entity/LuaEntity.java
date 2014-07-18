package net.lodoma.lime.physics.entity;

public class LuaEntity
{
    private Entity entity;
    
    public LuaEntity(Entity entity)
    {
        this.entity = entity;
    }
    
    public long getHash()
    {
        return entity.hash;
    }
}
