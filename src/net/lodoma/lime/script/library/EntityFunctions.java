package net.lodoma.lime.script.library;

import net.lodoma.lime.world.entity.Entity;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class EntityFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new EntityFunctions(library);
    }
    
    public LimeLibrary library;
    
    public Entity workingEntity;
    
    private EntityFunctions(LimeLibrary library)
    {
        this.library = library;
        
        for (FuncData func : FuncData.values())
            new LimeFunc(func).addToLibrary();
    }
    
    private class LimeFunc extends VarArgFunction
    {
        private FuncData data;
        
        public LimeFunc(FuncData data)
        {
            this.data = data;
        }
        
        public void addToLibrary()
        {
            library.table.set(data.name, this);
        }
        
        @Override
        public Varargs invoke(Varargs args)
        {
            if ((data.argcexact && args.narg() != data.argc) || (!data.argcexact && args.narg() < data.argc))
                throw new LuaError("invalid argument count to Lime function \"" + data.name + "\"");
            
            switch (data)
            {
            case SET_ENTITY:
            {
                int id = args.arg(1).checkinteger().toint();
                workingEntity = library.server.world.entityPool.get(id);
                if (workingEntity == null)
                    // TODO: throw something better
                    throw new RuntimeException("entity doesn't exist");
                break;
            }
            case SET_BODY_POSITION:
            {
                double x = args.arg(1).checknumber().todouble();
                double y = args.arg(2).checknumber().todouble();
                if (workingEntity == null)
                    // TODO: throw something better
                    throw new RuntimeException("working entity not set");
                workingEntity.body.position.set((float) x, (float) y);
                library.server.physicsEngine.updateQueue(workingEntity.identifier);
                break;
            }
            case SET_BODY_VELOCITY:
            {
                double x = args.arg(1).checknumber().todouble();
                double y = args.arg(2).checknumber().todouble();
                if (workingEntity == null)
                    // TODO: throw something better
                    throw new RuntimeException("working entity not set");
                workingEntity.body.velocity.set((float) x, (float) y);
                library.server.physicsEngine.updateQueue(workingEntity.identifier);
                break;
            }
            case SET_BODY_RADIUS:
            {
                double radius = args.arg(1).checknumber().todouble();
                if (workingEntity == null)
                    // TODO: throw something better
                    throw new RuntimeException("working entity not set");
                workingEntity.body.radius = (float) radius;
                
                workingEntity.body.mass = (float) (workingEntity.body.density * (4.0 / 3.0) * Math.PI * (radius * radius * radius));
                library.server.physicsEngine.updateQueue(workingEntity.identifier);
                break;
            }
            case SET_BODY_DENSITY:
            {
                double density = args.arg(1).checknumber().todouble();
                if (workingEntity == null)
                    // TODO: throw something better
                    throw new RuntimeException("working entity not set");
                workingEntity.body.density = (float) density;
                
                float radius = workingEntity.body.radius;
                workingEntity.body.mass = (float) (density * (4.0 / 3.0) * Math.PI * (radius * radius * radius));
                library.server.physicsEngine.updateQueue(workingEntity.identifier);
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        SET_ENTITY(1, true, "setEntity"),
        SET_BODY_POSITION(2, true, "setBodyPosition"),
        SET_BODY_VELOCITY(2, true, "setBodyVelocity"),
        SET_BODY_RADIUS(1, true, "setBodyRadius"),
        SET_BODY_DENSITY(1, true, "setBodyDensity");
        
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
