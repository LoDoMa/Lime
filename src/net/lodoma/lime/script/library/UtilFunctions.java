package net.lodoma.lime.script.library;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.Lime;
import net.lodoma.lime.logger.LogLevel;
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
        
        LogLevel[] levels = LogLevel.values();
        for (LogLevel level : levels)
            library.table.set("LOG_" + level.name(), LuaValue.valueOf(level.ordinal()));
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
            case MODULE:
            {
                String filepath = args.arg(1).checkstring().toString();
                LuaScript luaInstance = library.server.world.luaInstance;
                
                LuaTable moduleTable;
                if (luaInstance.globals.get("__LIME_MODULES__").isnil())
                {
                    moduleTable = LuaTable.tableOf();
                    luaInstance.globals.set("__LIME_MODULES__", moduleTable);
                }
                else moduleTable = luaInstance.globals.get("__LIME_MODULES__").checktable();
                
                if (moduleTable.get(filepath).isnil())
                {
                    LuaTable module = null;
                    try
                    {
                        luaInstance.load(new File(OsHelper.JARPATH + "script/module/" + filepath + ".lua"));
                        module = luaInstance.globals.get("__LIME_MODULE_TABLE__").checktable();
                        luaInstance.globals.set("__LIME_MODULE_TABLE__", LuaValue.NIL);
                    }
                    catch(IOException e)
                    {
                        Lime.LOGGER.C("Failed to include Lua file " + OsHelper.JARPATH + "script/include/" + filepath + ".lua");
                        Lime.LOGGER.log(e);
                        Lime.forceExit(e);
                    }
                    
                    moduleTable.set(filepath, module);
                }
                
                return moduleTable.get(filepath);
            }
            case LOG:
            {
                int level = args.arg(1).checkint();
                String message = args.arg(2).checkstring().tojstring();
                
                Lime.LOGGER.log(LogLevel.values()[level], message);
                break;
            }
            case CRASH:
            {
                Lime.forceExit(null);
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
        MODULE(1, true, "module"),
        LOG(2, true, "log"),
        CRASH(0, true, "crash");
        
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
