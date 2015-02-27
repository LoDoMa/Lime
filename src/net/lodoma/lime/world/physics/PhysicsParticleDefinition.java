package net.lodoma.lime.world.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import net.lodoma.lime.util.Vector2;

public class PhysicsParticleDefinition
{
    public Vector2 position = new Vector2(0.0f, 0.0f);
    public float angle;
    public float angularVelocity;
    public Vector2 linearVelocity = new Vector2(0.0f, 0.0f);
    
    public float size;
    public float density;
    public float restitution;
    
    public float angularDamping;
    public float linearDamping;
    
    public float lifetime;
    public boolean destroyOnCollision;
    
    public BodyDef engineBodyDefinition;
    public FixtureDef engineFixtureDefinition;
    
    public void validate() throws InvalidPhysicsParticleException
    {
        if (position == null)       throw new InvalidPhysicsParticleException("invalid particle position: null");
        if (linearVelocity == null) throw new InvalidPhysicsParticleException("invalid particle linear velocity: null");

        if (size < 0)               throw new InvalidPhysicsParticleException("invalid particle size: negative");
        if (density < 0)            throw new InvalidPhysicsParticleException("invalid particle density: negative");
        if (restitution < 0)        throw new InvalidPhysicsParticleException("invalid particle restitution: negative");
        
        if (angularDamping < 0)     throw new InvalidPhysicsParticleException("invalid particle angular damping: negative");
        if (linearDamping < 0)      throw new InvalidPhysicsParticleException("invalid particle linear damping: negative");
        
        if (lifetime < 0)           throw new InvalidPhysicsParticleException("invalid particle lifetime: negative");
    }
    
    public void create()
    {
        engineBodyDefinition = new BodyDef();
        engineBodyDefinition.position = position.toVec2();
        engineBodyDefinition.angle = angle;
        engineBodyDefinition.type = BodyType.DYNAMIC;
        engineBodyDefinition.angularVelocity = angularVelocity;
        engineBodyDefinition.linearVelocity = linearVelocity.toVec2();
        engineBodyDefinition.angularDamping = angularDamping;
        engineBodyDefinition.linearDamping = linearDamping;
        
        engineFixtureDefinition = new FixtureDef();
        engineFixtureDefinition.shape = new CircleShape();
        ((CircleShape) engineFixtureDefinition.shape).m_radius = size;
        engineFixtureDefinition.density = density;
        engineFixtureDefinition.friction = 0.0f;
        engineFixtureDefinition.restitution = restitution;
    }
}
