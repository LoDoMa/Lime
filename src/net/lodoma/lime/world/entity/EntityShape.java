package net.lodoma.lime.world.entity;

import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class EntityShape
{
    public PhysicsComponentSnapshot snapshots[];

    public void debugRender()
    {
        for (PhysicsComponentSnapshot snapshot : snapshots)
            snapshot.debugRender();
    }

    public void tempRender()
    {
        for (PhysicsComponentSnapshot snapshot : snapshots)
            snapshot.debugRender();
    }
}
