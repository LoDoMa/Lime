package net.lodoma.lime.script;

import org.jbox2d.dynamics.contacts.Contact;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import net.lodoma.lime.script.library.ContactTable;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.world.physics.PhysicsContactListener;

public class LuaContactListener extends PhysicsContactListener
{
    private LuaFunction preSolve;
    private LuaFunction postSolve;
    
    public LuaContactListener(Server server, Integer bodyA, Integer bodyB, LuaFunction preSolve, LuaFunction postSolve)
    {
        super(server, bodyA, bodyB);
        this.preSolve = preSolve;
        this.postSolve = postSolve;
    }
    
    @Override
    public void preSolve(int bodyA, int bodyB, Contact contact)
    {
        preSolve.invoke(new LuaValue[] { LuaValue.valueOf(bodyA), LuaValue.valueOf(bodyB), ContactTable.create(contact) });
    }

    @Override
    public void postSolve(int bodyA, int bodyB, Contact contact)
    {
        postSolve.invoke(new LuaValue[] { LuaValue.valueOf(bodyA), LuaValue.valueOf(bodyB), ContactTable.create(contact) });
    }
}
