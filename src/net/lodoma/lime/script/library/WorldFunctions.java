package net.lodoma.lime.script.library;

import net.lodoma.lime.Lime;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.entity.Entity;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class WorldFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new WorldFunctions(library);
    }
    
    public LimeLibrary library;
    public World world;
    
    private WorldFunctions(LimeLibrary library)
    {
        this.library = library;
        this.world = library.server.world;
        
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
            case SET_WORLD_GRAVITY:
            {
                float gravityX = args.arg(1).checknumber().tofloat();
                float gravityY = args.arg(2).checknumber().tofloat();
                library.server.world.physicsWorld.definition.gravity.set(gravityX, gravityY);
                break;
            }
            case NEW_ENTITY:
            {
                Entity entity = new Entity(world, library.server);
                int entityID = world.entityPool.add(entity);
                Lime.LOGGER.I("Created new entity " + entityID);
                return LuaValue.valueOf(entityID);
            }
            case REMOVE_ENTITY:
            {
                int entityID = args.arg(1).checkint();
                world.entityPool.get(entityID).destroy();
                world.entityPool.remove(entityID);
                Lime.LOGGER.I("Removed entity " + entityID);
                break;
            }
            case ASSIGN_SCRIPT:
            {
                int entityID = args.arg(1).checkint();
                String scriptName = args.arg(2).checkstring().tojstring();
                world.entityPool.get(entityID).assignScript(library.server, scriptName);
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        SET_WORLD_GRAVITY(2, true, "setWorldGravity"),

        NEW_ENTITY(0, true, "newEntity"),
        REMOVE_ENTITY(1, true, "removeEntity"),
        ASSIGN_SCRIPT(2, true, "assignScript");
        
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
