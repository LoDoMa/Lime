package net.lodoma.lime.script.library;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.Lime;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.OsHelper;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

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
                return LuaValue.valueOf(HashHelper.hash32(string));
            }
            case HASH64:
            {
                String string = args.arg(1).checkstring().toString();
                return LuaValue.valueOf(HashHelper.hash64(string));
            }
            case INCLUDE:
            {
                String filepath = args.arg(1).checkstring().toString();
                LuaScript luaInstance = library.server.world.luaInstance;
                
                LuaTable includeTable;
                if (luaInstance.globals.get("__LIME_INCLUDE__").isnil())
                {
                    includeTable = LuaTable.tableOf();
                    luaInstance.globals.set("__LIME_INCLUDE__", includeTable);
                }
                else includeTable = luaInstance.globals.get("__LIME_INCLUDE__").checktable();
                
                if (includeTable.get(filepath).isnil())
                {
                    try
                    {
                        luaInstance.load(new File(OsHelper.JARPATH + "script/include/" + filepath + ".lua"));
                    }
                    catch(IOException e)
                    {
                        Lime.LOGGER.C("Failed to include Lua file " + OsHelper.JARPATH + "script/include/" + filepath + ".lua");
                        Lime.LOGGER.log(e);
                        Lime.forceExit(e);
                    }
                    
                    includeTable.set(filepath, LuaValue.valueOf(true));
                }
                
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        HASH32(1, true, "hash32"),
        HASH64(1, true, "hash64"),
        INCLUDE(1, true, "include");
        
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
