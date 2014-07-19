package net.lodoma.lime.script;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.naef.jnlua.LuaState;

public class LuaScript
{
    private LuaState luaState;
    
    public LuaScript(File file, Object toLimeModule) throws IOException
    {
        this(new String(Files.readAllBytes(Paths.get(file.toURI()))), toLimeModule);
    }
    
    public LuaScript(String source, Object toLimeModule)
    {
        luaState = new LuaState();
        luaState.openLibs();
        
        luaState.pushJavaObject(toLimeModule);
        luaState.setGlobal("LIME_INIT");
        luaState.load("require \"script/lime\" LIME_INIT = nil", "requirement");
        luaState.call(0, 0);
        
        luaState.load(source, "script");
        luaState.call(0, 0);
    }
    
    public void call(String function, Object... arguments)
    {
        luaState.getGlobal(function);
        for(Object argument : arguments)
        {
                 if(argument instanceof Double) luaState.pushNumber((Double) argument);
            else if(argument instanceof Float) luaState.pushNumber((Float) argument);
            else if(argument instanceof String) luaState.pushString((String) argument);
        }
        
        luaState.call(arguments.length, 0);
    }
    
    public void close()
    {
        luaState.close();
    }
}
