package net.lodoma.lime.script;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.lodoma.lime.physics.entity.Entity;

import com.naef.jnlua.LuaState;

public class LuaScript
{
    private LuaState luaState;
    
    public LuaScript(File file, Entity entity) throws IOException
    {
        this(new String(Files.readAllBytes(Paths.get(file.toURI()))), entity);
    }
    
    public LuaScript(String source, Entity entity)
    {
        luaState = new LuaState();
        luaState.openLibs();

        luaState.pushJavaObject(entity);
        luaState.setGlobal("JAVA_ENTITY");
        luaState.pushJavaObject(this);
        luaState.setGlobal("JAVA_SCRIPT");
        luaState.load("require \"script/lime\" JAVA_ENTITY = nil JAVA_SCRIPT = nil", "requirement");
        luaState.call(0, 0);
        
        luaState.load(source, "script");
        luaState.call(0, 0);
    }
    
    public void call(String functionPath, Object... arguments)
    {
        String[] segm = functionPath.split("\\.");
        if(segm.length >= 1) luaState.getGlobal(segm[0]);
        for(int i = 1; i < segm.length; i++)
            luaState.getField(-1, segm[i]);
        for(Object argument : arguments)
        {
                 if(argument instanceof Double) luaState.pushNumber((Double) argument);
            else if(argument instanceof Float) luaState.pushNumber((Float) argument);
            else if(argument instanceof Integer) luaState.pushNumber((Integer) argument);
            else if(argument instanceof Long) luaState.pushNumber((Long) argument);
            else if(argument instanceof Boolean) luaState.pushBoolean((Boolean) argument);
            else if(argument instanceof String) luaState.pushString((String) argument);
            else luaState.pushJavaObject(argument);
        }
        luaState.call(arguments.length, 0);
        luaState.pop(segm.length - 1);
    }
    
    public void close()
    {
        luaState.close();
    }
}
