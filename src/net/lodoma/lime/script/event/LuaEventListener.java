package net.lodoma.lime.script.event;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class LuaEventListener extends EventListener
{
    public LuaFunction function;
    
    public LuaEventListener(LuaFunction function)
    {
        this.function = function;
    }
    
    @Override
    public void onEvent(Object... data)
    {
        LuaValue[] luaArguments = new LuaValue[data.length];
        for (int i = 0; i < data.length; i++)
            luaArguments[i] = CoerceJavaToLua.coerce(data[i]);
        
        Varargs returnedVarargs = function.invoke(luaArguments);
        if (returnedVarargs.narg() != 0)
            throw new LuaError("event function may not return anything");
    }
}
