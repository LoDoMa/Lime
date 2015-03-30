package net.lodoma.lime.script;

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
    public void preSolve(Contact contact, boolean swap)
    {
        if (preSolve != null)
            preSolve.invoke(new LuaValue[] { ContactTable.create(contact, swap) });
    }

    @Override
    public void postSolve(Contact contact, boolean swap)
    {
        if (postSolve != null)
            postSolve.invoke(new LuaValue[] { ContactTable.create(contact, swap) });
    }
    
    @Override
    public void beginContact(Contact contact, boolean swap)
    {
        if (beginContact != null)
            beginContact.invoke(new LuaValue[] { ContactTable.create(contact, swap) });
    }

    @Override
    public void endContact(Contact contact, boolean swap)
    {
        if (endContact != null)
            endContact.invoke(new LuaValue[] { ContactTable.create(contact, swap) });
    }
}
