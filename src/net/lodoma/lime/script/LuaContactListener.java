package net.lodoma.lime.script;

import org.jbox2d.dynamics.contacts.Contact;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import net.lodoma.lime.world.physics.PhysicsContactListener;

public class LuaContactListener extends PhysicsContactListener
{
    private LuaFunction preSolve;
    private LuaFunction postSolve;
    
    public LuaContactListener(LuaFunction preSolve, LuaFunction postSolve)
    {
        super();
        this.preSolve = preSolve;
        this.postSolve = postSolve;
    }
    
    public LuaContactListener(int bodyA, LuaFunction preSolve, LuaFunction postSolve)
    {
        super(bodyA);
        this.preSolve = preSolve;
        this.postSolve = postSolve;
    }
    
    public LuaContactListener(int bodyA, int bodyB, LuaFunction preSolve, LuaFunction postSolve)
    {
        super(bodyA, bodyB);
        this.preSolve = preSolve;
        this.postSolve = postSolve;
    }
    
    @Override
    public void preSolve(int bodyA, int bodyB, Contact contact)
    {
        preSolve.invoke(new LuaValue[] { LuaValue.valueOf(bodyA), LuaValue.valueOf(bodyB) });
    }

    @Override
    public void postSolve(int bodyA, int bodyB, Contact contact)
    {
        postSolve.invoke(new LuaValue[] { LuaValue.valueOf(bodyA), LuaValue.valueOf(bodyB) });
    }
}
