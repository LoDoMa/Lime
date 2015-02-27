package net.lodoma.lime.script;

import org.jbox2d.dynamics.contacts.Contact;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import net.lodoma.lime.script.library.ContactTable;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsContactListener;

public class LuaContactListener extends PhysicsContactListener
{
    private LuaFunction preSolve;
    private LuaFunction postSolve;
    
    public LuaContactListener(World world, Integer bodyA, Integer bodyB, LuaFunction preSolve, LuaFunction postSolve)
    {
        super(world, bodyA, bodyB);
        this.preSolve = preSolve;
        this.postSolve = postSolve;
    }
    
    @Override
    public void preSolve(PhysicsComponent bodyA, PhysicsComponent bodyB, Contact contact)
    {
        preSolve.invoke(new LuaValue[] { LuaValue.valueOf(bodyA.identifier), LuaValue.valueOf(bodyB.identifier), ContactTable.create(contact) });
    }

    @Override
    public void postSolve(PhysicsComponent bodyA, PhysicsComponent bodyB, Contact contact)
    {
        postSolve.invoke(new LuaValue[] { LuaValue.valueOf(bodyA.identifier), LuaValue.valueOf(bodyB.identifier), ContactTable.create(contact) });
    }
}
