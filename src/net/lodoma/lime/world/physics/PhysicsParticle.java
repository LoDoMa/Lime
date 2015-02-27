package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.lwjgl.opengl.GL11;

import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

public class PhysicsParticle implements Identifiable<Integer>
{
    public int identifier;
    
    public PhysicsWorld world;
    
    public Body engineBody;
    public Fixture engineFixture;

    public float size;
    public float lifetime;
    /** If true, the particle will be destroyed on collision with a physics component */
    public boolean destroyOnCollision;
    
    public boolean destroyed;
    
    public PhysicsParticle(PhysicsParticleDefinition definition, PhysicsWorld world)
    {
        this.world = world;
        
        engineBody = world.engineWorld.createBody(definition.engineBodyDefinition);
        engineFixture = engineBody.createFixture(definition.engineFixtureDefinition);
        
        size = definition.size;
        lifetime = definition.lifetime;
        destroyOnCollision = definition.destroyOnCollision;
        
        destroyed = false;
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
        
        // UserData for PhysicsParticles is the PhysicsParticle itself.
        engineBody.m_userData = this;
    }

    public void destroy()
    {
        engineBody.destroyFixture(engineFixture);
        world.engineWorld.destroyBody(engineBody);
        
        destroyed = true;
    }
    
    public void update(double timeDelta)
    {
        lifetime -= timeDelta;
        if (lifetime <= 0.0f) destroy();
    }
    
    public void debugRender()
    {
        Vector2 position = new Vector2(engineBody.getPosition());
        float angle = engineBody.getAngle();
        
        GL11.glPushMatrix();
        GL11.glTranslatef(position.x, position.y, 0.0f);
        GL11.glRotatef((float) Math.toDegrees(angle), 0.0f, 0.0f, 1.0f);
        GL11.glScalef(size, size, 1.0f);

        Texture.NO_TEXTURE.bind();

        GL11.glColor3f(0.7f, 0.7f, 0.7f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        GL11.glVertex2f(0.0f, 0.0f);
        for (int i = 0; i <= 10; i++)
        {
           float circleAngle = (float) Math.toRadians(i * 360.0 / 10.0);
           GL11.glVertex2f((float) Math.cos(circleAngle), (float) Math.sin(circleAngle));
        }
      
        GL11.glEnd();
        
        GL11.glPopMatrix();
    }
}
