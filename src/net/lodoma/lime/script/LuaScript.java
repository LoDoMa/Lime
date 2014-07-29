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
    }
    
    public void push(Object object)
    {
            if(object instanceof Boolean)   luaState.pushBoolean((Boolean) object);
       else if(object instanceof Byte)      luaState.pushNumber((Byte) object);
       else if(object instanceof Character) luaState.pushNumber((Character) object);
       else if(object instanceof Short)     luaState.pushNumber((Short) object);
       else if(object instanceof Integer)   luaState.pushNumber((Integer) object);
       else if(object instanceof Float)     luaState.pushNumber((Float) object);
       else if(object instanceof Long)      luaState.pushNumber((Long) object);
       else if(object instanceof Double)    luaState.pushNumber((Double) object);
       else if(object instanceof String)    luaState.pushString((String) object);
       else                                 luaState.pushJavaObject(object);
    }
    
    public void setGlobal(String name, Object value)
    {
        push(value);
        luaState.setGlobal("LIME_" + name);
    }
    
    public void removeGlobal(String name)
    {
        luaState.pushNil();
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
            push(argument);
        luaState.call(arguments.length, 0);
        luaState.pop(segm.length - 1);
    }
    
    public void close()
    {
        luaState.close();
    }
}
