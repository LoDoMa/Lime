package net.lodoma.lime.world.physics;

import java.util.function.Supplier;

public enum PhysicsComponentShapeType
{
    CIRCLE(() -> new PhysicsComponentCircleShape()),
    POLYGON(() -> new PhysicsComponentPolygonShape()),
    TRIANGLE_GROUP(() -> new PhysicsComponentTriangleGroupShape());
    
    public Supplier<PhysicsComponentShape> factory;
    
    private PhysicsComponentShapeType(Supplier<PhysicsComponentShape> factory)
    {
        this.factory = factory;
    }
}
