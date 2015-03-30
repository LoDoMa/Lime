package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.Fixture;
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
        Fixture fixtureA = contact.m_fixtureA;
        Fixture fixtureB = contact.m_fixtureB;
        PhysicsComponent bodyA = (PhysicsComponent) fixtureA.m_body.m_userData;
        PhysicsComponent bodyB = (PhysicsComponent) fixtureB.m_body.m_userData;
        
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else if (match == 1) preSolve(fixtureA, fixtureB, contact);
        else if (match == 2) preSolve(fixtureB, fixtureA, contact);
        else throw new IllegalStateException();
    }
    
    public void tryPostSolve(Contact contact)
    {
        Fixture fixtureA = contact.m_fixtureA;
        Fixture fixtureB = contact.m_fixtureB;
        PhysicsComponent bodyA = (PhysicsComponent) fixtureA.m_body.m_userData;
        PhysicsComponent bodyB = (PhysicsComponent) fixtureB.m_body.m_userData;
        
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else if (match == 1) postSolve(fixtureA, fixtureB, contact);
        else if (match == 2) postSolve(fixtureB, fixtureA, contact);
        else throw new IllegalStateException();
    }
    
    public void tryBeginContact(Contact contact)
    {
        Fixture fixtureA = contact.m_fixtureA;
        Fixture fixtureB = contact.m_fixtureB;
        PhysicsComponent bodyA = (PhysicsComponent) fixtureA.m_body.m_userData;
        PhysicsComponent bodyB = (PhysicsComponent) fixtureB.m_body.m_userData;
        
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else if (match == 1) beginContact(fixtureA, fixtureB, contact);
        else if (match == 2) beginContact(fixtureB, fixtureA, contact);
        else throw new IllegalStateException();
    }
    
    public void tryEndContact(Contact contact)
    {
        Fixture fixtureA = contact.m_fixtureA;
        Fixture fixtureB = contact.m_fixtureB;
        PhysicsComponent bodyA = (PhysicsComponent) fixtureA.m_body.m_userData;
        PhysicsComponent bodyB = (PhysicsComponent) fixtureB.m_body.m_userData;
        
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else if (match == 1) endContact(fixtureA, fixtureB, contact);
        else if (match == 2) endContact(fixtureB, fixtureA, contact);
        else throw new IllegalStateException();
    }
    
    /* For the next four methods, if the field bodyA is not null, the body of fixtureA argument
       must be equal to it. The same is true for bodyB. */
    
    public abstract void preSolve(Fixture fixtureA, Fixture fixtureB, Contact contact);
    public abstract void postSolve(Fixture fixtureA, Fixture fixtureB, Contact contact);
    public abstract void beginContact(Fixture fixtureA, Fixture fixtureB, Contact contact);
    public abstract void endContact(Fixture fixtureA, Fixture fixtureB, Contact contact);
    
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
