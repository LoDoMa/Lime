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
        int bodyA = (Integer) contact.m_fixtureA.m_body.m_userData;
        int bodyB = (Integer) contact.m_fixtureB.m_body.m_userData;
        contactListeners.foreach((PhysicsContactListener listener) -> listener.tryPreSolve(bodyA, bodyB, contact));
    }
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        int bodyA = (Integer) contact.m_fixtureA.m_body.m_userData;
        int bodyB = (Integer) contact.m_fixtureB.m_body.m_userData;
        contactListeners.foreach((PhysicsContactListener listener) -> listener.tryPostSolve(bodyA, bodyB, contact));
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
