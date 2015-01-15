package net.lodoma.lime.script.library;

import net.lodoma.lime.script.event.EventManager;
import net.lodoma.lime.script.event.LuaEventListener;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.IdentityPool;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class EventFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new EventFunctions(library);
    }
    
    public LimeLibrary library;
    public IdentityPool<EventManager> emanPool;
    
    private EventFunctions(LimeLibrary library)
    {
        this.library = library;
        this.emanPool = library.server.emanPool;
        
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
            case ADD_EVENT_LISTENER:
            {
                String eventName = args.arg(1).checkstring().tojstring();
                LuaFunction callback = args.arg(2).checkfunction();
                int hash = HashHelper.hash32(eventName);
                int listenerID = emanPool.get(hash).listeners.add(new LuaEventListener(callback));
                return LuaValue.valueOf(listenerID);
            }
            case REMOVE_EVENT_LISTENER:
            {
                int callbackID = args.arg(1).checkint();
                emanPool.remove(callbackID);
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        ADD_EVENT_LISTENER(2, true, "addEventListener"),
        REMOVE_EVENT_LISTENER(1, true, "removeEventListener");
        
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
