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
    
    public void tryPreSolve(PhysicsComponent bodyA, PhysicsComponent bodyB, Contact contact)
    {
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else if (match == 1) preSolve(bodyA, bodyB, contact);
        else if (match == 2) preSolve(bodyB, bodyA, contact);
        else throw new IllegalStateException();
    }
    
    public void tryPostSolve(PhysicsComponent bodyA, PhysicsComponent bodyB, Contact contact)
    {
        int match = matches(bodyA.identifier, bodyB.identifier);
        
        if (match == 0) return;
        else if (match == 1) postSolve(bodyA, bodyB, contact);
        else if (match == 2) postSolve(bodyB, bodyA, contact);
        else throw new IllegalStateException();
    }
    
    /* For preSolve() and postSolve(), if the field bodyA is not null, the bodyA argument
       must be equal to it. The same is true for bodyB. */
    
    public abstract void preSolve(PhysicsComponent bodyA, PhysicsComponent bodyB, Contact contact);
    public abstract void postSolve(PhysicsComponent bodyA, PhysicsComponent bodyB, Contact contact);
    
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
