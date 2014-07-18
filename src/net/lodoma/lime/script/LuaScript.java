package net.lodoma.lime.script;

import com.naef.jnlua.LuaState;

public class LuaScript
{
    private LuaState luaState;
    
    public LuaScript(String source)
    {
        luaState = new LuaState();
        luaState.openLibs();
        luaState.load(source, "=simple");
        luaState.call(0, 0);
    }
    
    public void call(String function)
    {
        luaState.getGlobal(function);
        luaState.call(0, 0);
    }
    
    public void close()
    {
        luaState.close();
    }
}
