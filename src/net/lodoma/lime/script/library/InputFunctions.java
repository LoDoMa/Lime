package net.lodoma.lime.script.library;

import java.lang.reflect.Field;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.server.logic.UserManager;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class InputFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new InputFunctions(library);
    }
    
    public LimeLibrary library;
    public UserManager userManager;
    
    private InputFunctions(LimeLibrary library)
    {
        this.library = library;
        this.userManager = library.server.userManager;
        
        for (FuncData func : FuncData.values())
            new LimeFunc(func).addToLibrary();
        
        try
        {
            Field[] inputFields = Input.class.getFields();
            for (Field field : inputFields)
                if (field.getName().startsWith("KEY_") ||
                    field.getName().startsWith("MOUSE_") ||
                    field.getName().startsWith("JOYSTICK_"))
                    library.table.set(field.getName(), CoerceJavaToLua.coerce(field.getInt(null)));
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
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
            case SET_INPUT_DATA:
            {
                int userID = args.arg(1).checkint();
                Input.inputData = userManager.getUser(userID).inputData;
                break;
            }
            case GET_KEY_DOWN:
            {
                int keyID = args.arg(1).checkint();
                return CoerceJavaToLua.coerce(Input.getKeyDown(keyID));
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        SET_INPUT_DATA(1, true, "setInputData"),
        GET_KEY_DOWN(1, true, "getKeyDown");
        
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
