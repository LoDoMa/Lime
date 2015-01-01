package net.lodoma.lime.world.entity;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.world.entity.physics.PhysicsWorld;

public class BodyComponent implements Identifiable<Integer>
{
    public int identifier;
    
    public PhysicsWorld world;
    
    public Body engineBody;
    public Fixture engineFixture;
    
    public BodyComponent(BodyComponentDefinition definition, PhysicsWorld world)
    {
        this.world = world;
        
        engineBody = world.engineWorld.createBody(definition.engineBodyDefinition);
        engineFixture = engineBody.createFixture(definition.engineFixtureDefinition);
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public void destroy()
    {
        engineBody.destroyFixture(engineFixture);
        world.engineWorld.destroyBody(engineBody);
    }
}
