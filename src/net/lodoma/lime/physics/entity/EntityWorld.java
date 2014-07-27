package net.lodoma.lime.physics.entity;

import net.lodoma.lime.common.NetworkSide;

public interface EntityWorld
{
    public void addEntity(Entity entity);
    public Entity getEntity(int id);
    public void removeEntity(int id);
    public NetworkSide getNetworkSide();
}
