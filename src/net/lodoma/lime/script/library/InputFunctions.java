package net.lodoma.lime.script.library;

import java.lang.reflect.Field;

import net.lodoma.lime.Lime;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.server.logic.UserManager;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

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
                    library.table.set(field.getName(), LuaValue.valueOf(field.getInt(null)));
        }
        catch (IllegalAccessException e)
        {
            Lime.LOGGER.C("Unexpected illegal access exception while getting constants from Input");
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
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
            case GET_KEY_STATE:
            {
                int keyID = args.arg(1).checkint();
                return LuaValue.valueOf(Input.getKey(keyID));
            }
            case GET_KEY_PRESS:
            {
                int keyID = args.arg(1).checkint();
                return LuaValue.valueOf(Input.getKeyDown(keyID));
            }
            case GET_KEY_RELEASE:
            {
                int keyID = args.arg(1).checkint();
                return LuaValue.valueOf(Input.getKeyUp(keyID));
            }
            case GET_MOUSE_POSITION:
            {
                return LuaValue.varargsOf(new LuaValue[] { LuaValue.valueOf(Input.getMousePosition().x), LuaValue.valueOf(Input.getMousePosition().y) });
            }
            case GET_MOUSE_STATE:
            {
                int mouseID = args.arg(1).checkint();
                return LuaValue.valueOf(Input.getMouse(mouseID));
            }
            case GET_MOUSE_PRESS:
            {
                int mouseID = args.arg(1).checkint();
                return LuaValue.valueOf(Input.getMouseDown(mouseID));
            }
            case GET_MOUSE_RELEASE:
            {
                int mouseID = args.arg(1).checkint();
                return LuaValue.valueOf(Input.getMouseUp(mouseID));
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        SET_INPUT_DATA(1, true, "setInputData"),
        GET_KEY_STATE(1, true, "getKeyState"),
        GET_KEY_PRESS(1, true, "getKeyPress"),
        GET_KEY_RELEASE(1, true, "getKeyRelease"),
        GET_MOUSE_POSITION(1, true, "getMousePosition"),
        GET_MOUSE_STATE(1, true, "getMouseState"),
        GET_MOUSE_PRESS(1, true, "getMousePress"),
        GET_MOUSE_RELEASE(1, true, "getMouseRelease");
        
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
