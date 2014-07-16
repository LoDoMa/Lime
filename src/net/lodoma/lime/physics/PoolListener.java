package net.lodoma.lime.physics;

public interface PoolListener
{
    public void onNewBody(PhysicsPool pool, PhysicsBody body);
    public void onNewJoint(PhysicsPool pool, PhysicsJoint joint);
}
