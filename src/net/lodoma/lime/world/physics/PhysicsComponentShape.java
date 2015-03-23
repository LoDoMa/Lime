package net.lodoma.lime.world.physics;

import org.jbox2d.collision.shapes.Shape;

public abstract class PhysicsComponentShape
{
    public abstract void validate() throws InvalidPhysicsComponentException;
    public abstract Shape[] newEngineInstances();
}
