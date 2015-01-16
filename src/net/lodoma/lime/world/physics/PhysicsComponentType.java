package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.BodyType;

public enum PhysicsComponentType
{
    DYNAMIC(BodyType.DYNAMIC),
    KINEMATIC(BodyType.KINEMATIC),
    STATIC(BodyType.STATIC);
    
    public final BodyType engineType;
    
    private PhysicsComponentType(BodyType engineType)
    {
        this.engineType = engineType;
    }
}
