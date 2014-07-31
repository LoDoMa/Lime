package net.lodoma.lime.script;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.naef.jnlua.LuaException;
import com.naef.jnlua.LuaState;

public class LuaScript
{
    public static boolean safeType(Object object)
    {
            if(object == null)              return true;
       else if(object instanceof Boolean)   return true;
       else if(object instanceof Byte)      return true;
       else if(object instanceof Character) return true;
       else if(object instanceof Short)     return true;
       else if(object instanceof Integer)   return true;
       else if(object instanceof Float)     return true;
       else if(object instanceof Long)      return true;
       else if(object instanceof Double)    return true;
       else if(object instanceof String)    return true;
       return false;
    }
    
    private LuaState luaState;
    
    public LuaScript()
    {
        luaState = new LuaState();
        luaState.openLibs();
    }
    
    public void push(Object object)
    {
            if(object == null)              luaState.pushNil();
       else if(object instanceof Boolean)   luaState.pushBoolean((Boolean) object);
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
    
    public void require(String file)
    {
        load("require \"" + file + "\"", "require");
    }
    
    public void load(File file) throws IOException
    {
        Path path = Paths.get(file.toURI());
        String source = new String(Files.readAllBytes(path));
        load(source);
    }
    
    public void load(String source)
    {
        load(source, "script");
    }
    
    private void load(String source, String chunk)
    {
        luaState.load(source, chunk);
        protectedCall(0, 0);
    }
    
    private void protectedCall(int argc, int retc)
    {
        try
        {
            luaState.call(argc, retc);
        }
        catch(LuaException e)
        {
            // TODO handle later
            e.printStackTrace();
        }
    }
    
    public void call(String functionPath, Object... arguments)
    {
        String[] segm = functionPath.split("\\.");
        if(segm.length >= 1) luaState.getGlobal(segm[0]);
        for(int i = 1; i < segm.length; i++)
            luaState.getField(-1, segm[i]);
        
        for(Object argument : arguments)
            push(argument);

        protectedCall(arguments.length, 0);
        luaState.pop(segm.length - 1);
    }
    
    public void close()
    {
        luaState.close();
    }
}
