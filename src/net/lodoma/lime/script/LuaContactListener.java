package net.lodoma.lime.script;

import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import net.lodoma.lime.script.library.ContactTable;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.physics.PhysicsContactListener;

public class LuaContactListener extends PhysicsContactListener
{
    private LuaFunction preSolve;
    private LuaFunction postSolve;
    private LuaFunction beginContact;
    private LuaFunction endContact;
    
    public LuaContactListener(World world, Integer bodyA, Integer bodyB,
            LuaFunction preSolve, LuaFunction postSolve,
            LuaFunction beginContact, LuaFunction endContact)
    {
        super(world, bodyA, bodyB);
        this.preSolve = preSolve;
        this.postSolve = postSolve;
        this.beginContact = beginContact;
        this.endContact = endContact;
    }
    
    @Override
    public void preSolve(Fixture fixtureA, Fixture fixtureB, Contact contact)
    {
        if (preSolve != null)
            preSolve.invoke(new LuaValue[] { ContactTable.create(fixtureA, fixtureB, contact) });
    }

    @Override
    public void postSolve(Fixture fixtureA, Fixture fixtureB, Contact contact)
    {
        if (postSolve != null)
            postSolve.invoke(new LuaValue[] { ContactTable.create(fixtureA, fixtureB, contact) });
    }
    
    @Override
    public void beginContact(Fixture fixtureA, Fixture fixtureB, Contact contact)
    {
        if (beginContact != null)
            beginContact.invoke(new LuaValue[] { ContactTable.create(fixtureA, fixtureB, contact) });
    }

    @Override
    public void endContact(Fixture fixtureA, Fixture fixtureB, Contact contact)
    {
        if (endContact != null)
            endContact.invoke(new LuaValue[] { ContactTable.create(fixtureA, fixtureB, contact) });
    }
}
