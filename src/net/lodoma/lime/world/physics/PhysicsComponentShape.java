package net.lodoma.lime.world.physics;

import org.jbox2d.collision.shapes.Shape;

public abstract class PhysicsComponentShape
{
    public abstract Shape newEngineInstance();
}
