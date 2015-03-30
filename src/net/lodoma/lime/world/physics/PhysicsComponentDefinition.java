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
    public float density;
    public float friction;
    public float restitution;
    
    public BodyDef engineBodyDefinition;
    public FixtureDef[] engineFixtureDefinitions;
    
    public void validate() throws InvalidPhysicsComponentException
    {
        if (position == null)   throw new InvalidPhysicsComponentException("invalid component position: null");
        if (type == null)       throw new InvalidPhysicsComponentException("invalid component type: null");

        if (shapes == null)     throw new InvalidPhysicsComponentException("invalid component shape list: null");
        if (shapes.size() <= 0) throw new InvalidPhysicsComponentException("invalid component shape list: empty");
        if (density < 0)        throw new InvalidPhysicsComponentException("invalid component density: negative");
        if (friction < 0)       throw new InvalidPhysicsComponentException("invalid component friction: negative");
        if (restitution < 0)    throw new InvalidPhysicsComponentException("invalid component restitution: negative");
        
        for (PhysicsShape shape : shapes)
            shape.validate();
    }
    
    public void create()
    {
        engineBodyDefinition = new BodyDef();
        engineBodyDefinition.position = position.toVec2();
        engineBodyDefinition.angle = angle;
        engineBodyDefinition.type = type.engineType;
        
        List<Shape> engineShapes = new ArrayList<Shape>();
        for (PhysicsShape shape : shapes)
        {
            Shape[] engineInstances = shape.newEngineInstances();
            for (Shape engineInstance : engineInstances)
                engineShapes.add(engineInstance);
        }
        
        engineFixtureDefinitions = new FixtureDef[engineShapes.size()];
        
        for (int i = 0; i < engineFixtureDefinitions.length; i++)
        {
            engineFixtureDefinitions[i] = new FixtureDef();
            engineFixtureDefinitions[i].shape = engineShapes.get(i);
            engineFixtureDefinitions[i].density = density;
            engineFixtureDefinitions[i].friction = friction;
            engineFixtureDefinitions[i].restitution = restitution;
        }
    }
}
