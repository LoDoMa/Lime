package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.IdentityPool;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsContactManager implements ContactListener
{
    public IdentityPool<PhysicsContactListener> contactListeners;
    
    public PhysicsContactManager()
    {
        contactListeners = new IdentityPool<PhysicsContactListener>(false);
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        Object userdataA = contact.m_fixtureA.m_body.m_userData;
        Object userdataB = contact.m_fixtureB.m_body.m_userData;
        if ((userdataA instanceof PhysicsParticle) && (userdataB instanceof PhysicsParticle))
        {
            contact.setEnabled(false);
        }
        else if ((userdataA instanceof PhysicsParticle) && (userdataB instanceof PhysicsComponent))
        {
            if (((PhysicsParticle) userdataA).destroyOnCollision)
                ((PhysicsParticle) userdataA).destroy();
        }
        else if ((userdataA instanceof PhysicsComponent) && (userdataB instanceof PhysicsParticle))
        {
            if (((PhysicsParticle) userdataB).destroyOnCollision)
                ((PhysicsParticle) userdataB).destroy();
        }
        else if ((userdataA instanceof PhysicsComponent) && (userdataB instanceof PhysicsComponent))
        {
            contactListeners.foreach((PhysicsContactListener listener) -> listener.tryPreSolve((PhysicsComponent) userdataA, (PhysicsComponent) userdataB, contact));
        }
    }
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        Object userdataA = contact.m_fixtureA.m_body.m_userData;
        Object userdataB = contact.m_fixtureB.m_body.m_userData;
        if ((userdataA instanceof PhysicsParticle) && (userdataB instanceof PhysicsParticle));
            // particle - particle collision, do nothing
        else if ((userdataA instanceof PhysicsParticle) && (userdataB instanceof PhysicsComponent))
        {
            if (((PhysicsParticle) userdataA).destroyOnCollision)
                ((PhysicsParticle) userdataA).destroy();
        }
        else if ((userdataA instanceof PhysicsComponent) && (userdataB instanceof PhysicsParticle))
        {
            if (((PhysicsParticle) userdataB).destroyOnCollision)
                ((PhysicsParticle) userdataB).destroy();
        }
        else if ((userdataA instanceof PhysicsComponent) && (userdataB instanceof PhysicsComponent))
        {
            contactListeners.foreach((PhysicsContactListener listener) -> listener.tryPostSolve((PhysicsComponent) userdataA, (PhysicsComponent) userdataB, contact));
        }
    }
    
    @Override
    public void beginContact(Contact contact)
    {
        
    }
    
    @Override
    public void endContact(Contact contact)
    {
        
    }
}
