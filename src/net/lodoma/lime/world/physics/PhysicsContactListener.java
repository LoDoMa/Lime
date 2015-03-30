package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.contacts.Contact;

import net.lodoma.lime.Lime;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.world.World;

public abstract class PhysicsContactListener implements Identifiable<Integer>
{
    public int identifier;
    
    public World world;
    
    /* Objects so that they can have the null value. */
    public Integer bodyA;
    public Integer bodyB;
    
    public PhysicsContactListener(World world, Integer bodyA, Integer bodyB)
    {
        this.world = world;
        this.bodyA = bodyA;
        this.bodyB = bodyB;

        if (bodyA != null) world.componentPool.get(bodyA).contactListeners.add(this);
        if (bodyB != null) world.componentPool.get(bodyB).contactListeners.add(this);
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
        if (bodyA != null) world.componentPool.get(bodyA).contactListeners.remove(this);
        if (bodyB != null) world.componentPool.get(bodyB).contactListeners.remove(this);
        world.physicsWorld.contactManager.contactListeners.remove(this);
        
        Lime.LOGGER.I("Destroyed contact listener " + identifier);
    }
    
    public void tryPreSolve(Contact contact)
    {
        PhysicsComponent bodyA = (PhysicsComponent) contact.m_fixtureA.m_body.m_userData;
        PhysicsComponent bodyB = (PhysicsComponent) contact.m_fixtureB.m_body.m_userData;
        
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else preSolve(contact, match == 2);
    }
    
    public void tryPostSolve(Contact contact)
    {
        PhysicsComponent bodyA = (PhysicsComponent) contact.m_fixtureA.m_body.m_userData;
        PhysicsComponent bodyB = (PhysicsComponent) contact.m_fixtureB.m_body.m_userData;
        
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else postSolve(contact, match == 2);
    }
    
    public void tryBeginContact(Contact contact)
    {
        PhysicsComponent bodyA = (PhysicsComponent) contact.m_fixtureA.m_body.m_userData;
        PhysicsComponent bodyB = (PhysicsComponent) contact.m_fixtureB.m_body.m_userData;
        
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else beginContact(contact, match == 2);
    }
    
    public void tryEndContact(Contact contact)
    {
        PhysicsComponent bodyA = (PhysicsComponent) contact.m_fixtureA.m_body.m_userData;
        PhysicsComponent bodyB = (PhysicsComponent) contact.m_fixtureB.m_body.m_userData;
        
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else endContact(contact, match == 2);
    }
    
    /* For the next four methods, if the field bodyA is not null, the body of fixtureA argument
       must be equal to it. The same is true for bodyB. */
    
    public abstract void preSolve(Contact contact, boolean swap);
    public abstract void postSolve(Contact contact, boolean swap);
    public abstract void beginContact(Contact contact, boolean swap);
    public abstract void endContact(Contact contact, boolean swap);
    
    /* Returns 0 if the bodies don't match this listener's filter, 1 if they match
       perfectly, or 2 if the bodies need to be swapped. */
    private int matches(int bodyA, int bodyB)
    {
        if (this.bodyA == null)
            if (this.bodyB == null) return 1;
            else if (this.bodyB == bodyB) return 1;
            else if (this.bodyB == bodyA) return 2;
            else return 0;
        else if (this.bodyB == null)
            if (this.bodyA == bodyA) return 1;
            else if (this.bodyA == bodyB) return 2;
            else return 0;
        else
            if (this.bodyA == bodyA && this.bodyB == bodyB) return 1;
            else if (this.bodyA == bodyB && this.bodyB == bodyA) return 2;
            else return 0;
    }
}
