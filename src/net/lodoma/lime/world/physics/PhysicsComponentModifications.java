package net.lodoma.lime.world.physics;

public class PhysicsComponentModifications
{
    public boolean positionModified;
    public boolean rotationModified;
    public boolean shapeModified;
    
    public PhysicsComponentSnapshot data;
    
    public PhysicsComponentModifications()
    {
        data = new PhysicsComponentSnapshot();
    }
    
    public void apply(PhysicsComponentSnapshot compo)
    {
        if (positionModified)
        {
            compo.position = data.position;
        }
        
        if (rotationModified)
            compo.angle = data.angle;
        
        if (shapeModified)
        {
            compo.type = data.type;
            compo.radius = data.radius;
            compo.vertices = data.vertices;
        }
    }
}
