package net.lodoma.lime.script.library;

import net.lodoma.lime.world.physics.PhysicsComponent;

import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class ContactTable
{
    public static synchronized LuaTable create(Fixture fixtureA, Fixture fixtureB, Contact contact)
    {
        return new ContactTable(fixtureA, fixtureB, contact).table;
    }

    private LuaTable table;
    private Contact contact;
    
    public ContactTable(Fixture fixtureA, Fixture fixtureB, Contact contact)
    {
        table = LuaTable.tableOf();
        this.contact = contact;

        PhysicsComponent bodyA = (PhysicsComponent) fixtureA.m_body.m_userData;
        PhysicsComponent bodyB = (PhysicsComponent) fixtureB.m_body.m_userData;
        table.set("bodyA", bodyA.identifier);
        table.set("bodyB", bodyB.identifier);
        
        if (fixtureA.m_userData != null)
            table.set("fixtureA", LuaValue.valueOf((String) fixtureA.m_userData));
        if (fixtureB.m_userData != null)
            table.set("fixtureB", LuaValue.valueOf((String) fixtureB.m_userData));
        
        for (FuncData func : FuncData.values())
            new LimeFunc(func).addToTable();
    }
    
    private class LimeFunc extends VarArgFunction
    {
        private FuncData data;
        
        public LimeFunc(FuncData data)
        {
            this.data = data;
        }
        
        public void addToTable()
        {
            table.set(data.name, this);
        }
        
        @Override
        public Varargs invoke(Varargs args)
        {
            if ((data.argcexact && args.narg() != data.argc) || (!data.argcexact && args.narg() < data.argc))
                throw new LuaError("invalid argument count to Lime function \"" + data.name + "\"");
            
            switch (data)
            {
            case IS_ENABLED:
            {
                return LuaValue.valueOf(contact.isEnabled());
            }
            case SET_ENABLED:
            {
                boolean enabled = args.arg(1).checkboolean();
                contact.setEnabled(enabled);
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        IS_ENABLED(0, true, "isEnabled"),
        SET_ENABLED(1, true, "setEnabled");
        
        public int argc;
        public boolean argcexact;
        public String name;
        
        private FuncData(int argc, boolean argcexact, String name)
        {
            this.argc = argc;
            this.argcexact = argcexact;
            this.name = name;
        }
    }
}
