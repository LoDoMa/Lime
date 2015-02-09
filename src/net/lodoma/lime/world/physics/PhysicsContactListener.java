package net.lodoma.lime.world.physics;

import org.jbox2d.dynamics.contacts.Contact;

import net.lodoma.lime.util.Identifiable;

public abstract class PhysicsContactListener implements Identifiable<Integer>
{
    public int identifier;
    
    /**
     * 0 = no filter
     * 1 = contact must include bodyA
     * 2 = contact must include both bodyA and bodyB
     */
    public int filterLevel;
    public int bodyA;
    public int bodyB;
    
    public PhysicsContactListener()
    {
        filterLevel = 0;
    }
    
    public PhysicsContactListener(int bodyA)
    {
        filterLevel = 1;
        this.bodyA = bodyA;
    }
    
    public PhysicsContactListener(int bodyA, int bodyB)
    {
        filterLevel = 2;
        this.bodyA = bodyA;
        this.bodyB = bodyB;
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
    
    public abstract void preSolve(int bodyA, int bodyB, Contact contact);
    public abstract void postSolve(int bodyA, int bodyB, Contact contact);
}
