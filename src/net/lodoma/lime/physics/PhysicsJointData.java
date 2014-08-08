package net.lodoma.lime.physics;

import net.lodoma.lime.util.Vector2;

public class PhysicsJointData
{
    public PhysicsJointType jointType;
    public int bodyA;
    public int bodyB;
    public boolean collide;

    public Vector2 anchorA;
    public Vector2 anchorB;
    public float angle;
    public boolean motorEnabled;
    public float motorSpeed;
    public float maxMotorTorque;
    public boolean limitEnabled;
    public float lowLimit;
    public float highLimit;
}
