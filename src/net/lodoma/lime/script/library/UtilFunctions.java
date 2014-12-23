package net.lodoma.lime.script.library;

import net.lodoma.lime.util.HashHelper;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class UtilFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new UtilFunctions(library);
    }
    
    public LimeLibrary library;
    
    private UtilFunctions(LimeLibrary library)
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
            case HASH32:
            {
                String string = args.arg(1).checkstring().toString();
                return CoerceJavaToLua.coerce(HashHelper.hash32(string));
            }
            case HASH64:
            {
                String string = args.arg(1).checkstring().toString();
                return CoerceJavaToLua.coerce(HashHelper.hash64(string));
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        HASH32(1, true, "hash32"),
        HASH64(1, true, "hash64");
        
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
