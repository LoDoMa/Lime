package net.lodoma.lime.script;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.naef.jnlua.LuaState;

public class LuaScript
{
    private LuaState luaState;
    
    public LuaScript()
    {
        luaState = new LuaState();
        luaState.openLibs();
        
        setGlobal("SCRIPT", this);
    }
    
    public void setGlobal(String name, Object value)
    {
        luaState.pushJavaObject(value);
        luaState.setGlobal("LIME_" + name);
    }
    
    public void require(String file)
    {
        luaState.load("require \"" + file + "\"", "requirement");
        luaState.call(0, 0);
    }
    
    public void load(String source)
    {
        luaState.load(source, "script");
        luaState.call(0, 0);
    }
    
    public void load(File file) throws IOException
    {
        load(new String(Files.readAllBytes(Paths.get(file.toURI()))));
    }
    
    public void call(String functionPath, Object... arguments)
    {
        String[] segm = functionPath.split("\\.");
        if(segm.length >= 1) luaState.getGlobal(segm[0]);
        for(int i = 1; i < segm.length; i++)
            luaState.getField(-1, segm[i]);
        for(Object argument : arguments)
        {
                 if(argument instanceof Boolean)   luaState.pushBoolean((Boolean) argument);
            else if(argument instanceof Byte)      luaState.pushNumber((Byte) argument);
            else if(argument instanceof Character) luaState.pushNumber((Character) argument);
            else if(argument instanceof Short)     luaState.pushNumber((Short) argument);
            else if(argument instanceof Integer)   luaState.pushNumber((Integer) argument);
            else if(argument instanceof Float)     luaState.pushNumber((Float) argument);
            else if(argument instanceof Long)      luaState.pushNumber((Long) argument);
            else if(argument instanceof Double)    luaState.pushNumber((Double) argument);
            else if(argument instanceof String)    luaState.pushString((String) argument);
            else                                   luaState.pushJavaObject(argument);
        }
        luaState.call(arguments.length, 0);
        luaState.pop(segm.length - 1);
    }
    
    public void close()
    {
        luaState.close();
    }
}
