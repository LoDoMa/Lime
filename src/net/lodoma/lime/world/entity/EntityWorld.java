package net.lodoma.lime.world.entity;

import net.lodoma.lime.physics.entity.Entity;

public interface EntityWorld
{
    public void addEntity(Entity entity);
    public Entity getEntity(long id);
    public void removeEntity(long id);
}
