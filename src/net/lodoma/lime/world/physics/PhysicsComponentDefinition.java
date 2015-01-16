package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsComponentDefinition
{
    public Vector2 position = new Vector2(0.0f, 0.0f);
    public float angle;
    
    public float radius;
    public float density;
    public float friction;
    public float restitution;
    
    public BodyDef engineBodyDefinition;
    public FixtureDef engineFixtureDefinition;
    
    public void create()
    {
        engineBodyDefinition = new BodyDef();
        engineBodyDefinition.position = position.toVec2();
        engineBodyDefinition.angle = angle;
        engineBodyDefinition.type = BodyType.DYNAMIC;
        
        engineFixtureDefinition = new FixtureDef();
        engineFixtureDefinition.shape = new CircleShape();
        ((CircleShape) engineFixtureDefinition.shape).m_radius = radius;
        engineFixtureDefinition.density = density;
        engineFixtureDefinition.friction = friction;
        engineFixtureDefinition.restitution = restitution;
    }
}
