package net.lodoma.lime.world.physics;

import net.lodoma.lime.util.IdentityPool;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsContactManager implements ContactListener
{
    public IdentityPool<PhysicsContactListener> listeners;
    
    public PhysicsContactManager()
    {
        listeners = new IdentityPool<PhysicsContactListener>(false);
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        int bodyA = (Integer) contact.m_fixtureA.m_body.m_userData;
        int bodyB = (Integer) contact.m_fixtureB.m_body.m_userData;
        listeners.foreach((PhysicsContactListener listener) -> {
            if ((listener.filterLevel == 0) ||
                (listener.filterLevel == 1 && (bodyA == listener.bodyA || bodyB == listener.bodyA)) ||
                (listener.filterLevel == 2 && ((bodyA == listener.bodyA && bodyB == listener.bodyB) ||
                                               (bodyB == listener.bodyA && bodyA == listener.bodyB))))
                listener.preSolve(bodyA, bodyB, contact);
        });
    }
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        int bodyA = (Integer) contact.m_fixtureA.m_body.m_userData;
        int bodyB = (Integer) contact.m_fixtureB.m_body.m_userData;
        listeners.foreach((PhysicsContactListener listener) -> {
            if ((listener.filterLevel == 0) ||
                (listener.filterLevel == 1 && (bodyA == listener.bodyA || bodyB == listener.bodyA)) ||
                (listener.filterLevel == 2 && ((bodyA == listener.bodyA && bodyB == listener.bodyB) ||
                                               (bodyB == listener.bodyA && bodyA == listener.bodyB))))
                listener.postSolve(bodyA, bodyB, contact);
        });
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
