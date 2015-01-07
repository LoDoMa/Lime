package net.lodoma.lime.script.library;

import net.lodoma.lime.world.World;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class AttributeFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new AttributeFunctions(library);
    }
    
    public LimeLibrary library;
    public World world;
    
    private AttributeFunctions(LimeLibrary library)
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
            case GET_ATTRIBUTE:
            {
                int entityID = args.arg(1).checkint();
                String attribName = args.arg(2).checkstring().tojstring();
                return CoerceJavaToLua.coerce(world.entityPool.get(entityID).attributes.values.get(attribName));
            }
            case SET_ATTRIBUTE:
            {
                int entityID = args.arg(1).checkint();
                String attribName = args.arg(2).checkstring().tojstring();
                LuaValue value = args.arg(3);
                if (value.isnil())
                	world.entityPool.get(entityID).attributes.values.remove(attribName);
                else
                	world.entityPool.get(entityID).attributes.values.put(attribName, value);
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        GET_ATTRIBUTE(2, true, "getAttribute"),
        SET_ATTRIBUTE(3, true, "setAttribute");
        
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
