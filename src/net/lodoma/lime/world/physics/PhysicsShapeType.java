package net.lodoma.lime.world.physics;

import java.util.function.Supplier;

public enum PhysicsShapeType
{
    CIRCLE(() -> new PhysicsShapeCircle()),
    POLYGON(() -> null),
    TRIANGLE_GROUP(() -> new PhysicsShapeTriangleGroup());
    
    public Supplier<PhysicsShape> factory;
    
    private PhysicsShapeType(Supplier<PhysicsShape> factory)
    {
        this.factory = factory;
    }
}
