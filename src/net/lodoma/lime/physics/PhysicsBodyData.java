package net.lodoma.lime.physics;

import net.lodoma.lime.util.Vector2;

public class PhysicsBodyData
{
    public Vector2 position;
    public float angle;
    public PhysicsBodyType bodyType;
    
    public ShapeType shapeType;
    public float shapeRadius;
    public Vector2[] shapeVertices;
    
    public float density;
    public float friction;
    public float restitution;
}
