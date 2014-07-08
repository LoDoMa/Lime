package net.lodoma.lime.physics;

import org.jbox2d.dynamics.BodyType;

public enum PhysicsBodyType
{
    DYNAMIC(BodyType.DYNAMIC),
    STATIC(BodyType.STATIC),
    KINEMATIC(BodyType.KINEMATIC);
    
    private BodyType engineValue;
    
    private PhysicsBodyType(BodyType engineValue)
    {
        this.engineValue = engineValue;
    }
    
    public BodyType getEngineValue()
    {
        return engineValue;
    }
}
