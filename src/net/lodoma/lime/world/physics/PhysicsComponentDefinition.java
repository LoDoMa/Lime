package net.lodoma.lime.world.physics;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsComponentDefinition
{
    public Vector2 position = new Vector2(0.0f, 0.0f);
    public float angle;
    public PhysicsComponentType type;
    
    public List<PhysicsShape> shapes = new ArrayList<PhysicsShape>();
    
    public BodyDef engineBodyDefinition;
    public FixtureDef[] engineFixtureDefinitions;
    
    public void validate() throws InvalidPhysicsComponentException
    {
        if (position == null)   throw new InvalidPhysicsComponentException("invalid component position: null");
        if (type == null)       throw new InvalidPhysicsComponentException("invalid component type: null");

        if (shapes == null)     throw new InvalidPhysicsComponentException("invalid component shape list: null");
        if (shapes.size() <= 0) throw new InvalidPhysicsComponentException("invalid component shape list: empty");
        
        for (PhysicsShape shape : shapes)
        {
            try
            {
                shape.validate();
            }
            catch (InvalidPhysicsShapeException ex)
            {
                throw new InvalidPhysicsComponentException(ex);
            }
        }
    }
    
    public void create()
    {
        engineBodyDefinition = new BodyDef();
        engineBodyDefinition.position = position.toVec2();
        engineBodyDefinition.angle = angle;
        engineBodyDefinition.type = type.engineType;
        
        int instanceCount = 0;
        for (PhysicsShape shape : shapes)
        {
            shape.newEngineInstances();
            instanceCount += shape.engineInstances.length;
        }
        
        engineFixtureDefinitions = new FixtureDef[instanceCount];

        int i = 0;
        for (PhysicsShape shape : shapes)
            for (Shape engineInstance : shape.engineInstances)
            {
                engineFixtureDefinitions[i] = new FixtureDef();
                engineFixtureDefinitions[i].userData = shape.name;
                engineFixtureDefinitions[i].shape = engineInstance;
                engineFixtureDefinitions[i].isSensor = !shape.isSolid;
                engineFixtureDefinitions[i].density = shape.density;
                engineFixtureDefinitions[i].friction = shape.friction;
                engineFixtureDefinitions[i].restitution = shape.restitution;
                i++;
            }
    }
}
