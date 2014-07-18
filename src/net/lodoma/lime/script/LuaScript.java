package net.lodoma.lime.script;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.naef.jnlua.LuaState;

public class LuaScript
{
    private LuaState luaState;
    
    public LuaScript(File file) throws IOException
    {
        this(new String(Files.readAllBytes(Paths.get(file.toURI()))));
    }
    
    public LuaScript(String source)
    {
        luaState = new LuaState();
        luaState.openLibs();
        luaState.load(source, "=simple");
        luaState.call(0, 0);
    }
    
    public void call(String function, double argument)
    {
        luaState.getGlobal(function);
        luaState.pushNumber(argument);
        luaState.call(1, 0);
    }
    
    public void close()
    {
        luaState.close();
    }
}
