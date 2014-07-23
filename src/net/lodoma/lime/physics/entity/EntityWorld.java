package net.lodoma.lime.physics.entity;


public interface EntityWorld
{
    public void addEntity(Entity entity);
    public Entity getEntity(int id);
    public void removeEntity(int id);
    public boolean isServer();
}
