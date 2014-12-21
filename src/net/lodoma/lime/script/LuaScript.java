package net.lodoma.lime.script;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.naef.jnlua.LuaException;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;

/**
 * LuaScript is a class for managing a Lua state.
 * 
 * @author Lovro Kalinovčić
 */
public class LuaScript
{
    /**
     * Returns if an object is of a safe type.
     * A safe type is a type that has its Lua alternative.
     * In other words, non-safe types are userdata in Lua.
     * Safe types are: boolean, byte, chat, short, int,
     *                 float, long, double and String.
     * 
     * @param object - the object whose type is tested
     * @return if the object is of a safe type
     */
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
    
    /**
     * Creates a new Lua state and opens libraries.
     */
    public LuaScript()
    {
        luaState = new LuaState();
        luaState.openLibs();
    }
    
    /**
     * Pushes an object onto the Lua stack.
     * Null is pushed as nil.
     * Boolean is pushed as boolean.
     * Byte, char, short, int, float, long and double are pushed as numbers.
     * String is pushed as string.
     * All other types are pushed as Java objects.
     * 
     * @param object - the object to push
     */
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
    
    /**
     * Pops a value from the stack.
     * This value is either a boolean, a number, a string or null.
     * 
     * @return the value from the top of the stack.
     */
    public Object pop()
    {
        Object returned = popBoolean();
        if(returned != null) return returned;
        returned = popNumber();
        if(returned != null) return returned;
        returned = popString();
        return returned;
    }
    
    private Boolean popBoolean()
    {
        try
        {
            luaState.checkType(-1, LuaType.BOOLEAN);
            return luaState.toBoolean(-1);
        }
        catch(LuaRuntimeException e)
        {
            return null;
        }
    }
    
    private Double popNumber()
    {
        try
        {
            luaState.checkType(-1, LuaType.NUMBER);
            return luaState.toNumber(-1);
        }
        catch(LuaRuntimeException e)
        {
            return null;
        }
    }
    
    private String popString()
    {
        try
        {
            luaState.checkType(-1, LuaType.STRING);
            return luaState.toString(-1);
        }
        catch(LuaRuntimeException e)
        {
            return null;
        }
    }
    
    /**
     * Sets a global in Lua state.
     * The global name is prefixed by "LIME_".
     * Globals value is pushed onto the stack using the "push" method.
     * 
     * @param name - name of the global (note that it is prefixed by "LIME_")
     * @param value - initial value of the global
     */
    public void setGlobal(String name, Object value)
    {
        push(value);
        luaState.setGlobal("LIME_" + name);
    }
    
    /**
     * Requires a file in Lua state.
     * The requirement chunk is called "require".
     * 
     * @param file - path to the required file
     */
    public void require(String file)
    {
        load("require \"" + file + "\"", "require");
    }
    
    /**
     * Loads the source from a file.
     * The loading is done using the "load(String)" method.
     * 
     * @param file - file to read from
     * @throws IOException
     */
    public void load(File file) throws IOException
    {
        Path path = Paths.get(file.toURI());
        String source = new String(Files.readAllBytes(path));
        load(source, file.getPath());
    }
    
    /**
     * Loads some source in Lua state.
     * The source chunk is called "source".
     * 
     * @param source - the source to be loaded.
     */
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
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e1)
            {
                e1.printStackTrace();
            }
        }
    }
    
    /**
     * Calls a function specified by its path, taking given parameters.
     * The function is expected to not return anything.
     * 
     * This method simply calls the "call(String, int, Object[])"
     * method, with 0 provided as the return count.
     * 
     * @param functionPath - path of the function
     * @param arguments - arguments to the function
     */
    public void call(String functionPath, Object[] arguments)
    {
        call(functionPath, 0, arguments);
    }
    
    /**
     * Calls a function specified by its path.
     * A function path is built of names separated by ".".
     * 
     * The first name is a name of a global.
     * The second name (if it exists) is a name of a table entry inside the global.
     * The third name (if it exists) is a name of a table entry inside the previous table.
     * This repeats to the last name.
     * 
     * Value of the last name must be a function.
     * Values of all other names must be tables.
     * 
     * This function takes arguments from the object array.
     * Objects in the object array are pushed to the stack
     * (and so converted to Lua types) using the "push" method.
     * 
     * Everything returned by this function is returned in an
     * object array. Lua types are popped from the stack
     * (and so converted to Java types) using the "pop" method.
     * 
     * @param functionPath - path of the function
     * @param returnc - count of returned elements
     * @param arguments - arguments to the function
     * @return an object array with "returnc" elements - everything returned by the function
     */
    public Object[] call(String functionPath, int returnc, Object[] arguments)
    {
        if(arguments == null)
            arguments = new Object[0];
        
        String[] segm = functionPath.split("\\.");
        if(segm.length >= 1) luaState.getGlobal(segm[0]);
        for(int i = 1; i < segm.length; i++)
            luaState.getField(-1, segm[i]);
        
        for(Object argument : arguments)
            push(argument);

        protectedCall(arguments.length, returnc);
        
        Object[] returned = new Object[returnc];
        for(int i = 0; i < returnc; i++)
            returned[i] = pop();
        
        luaState.pop(segm.length - 1);
        
        return returned;
    }
    
    /**
     * Closes the Lua state.
     */
    public void close()
    {
        luaState.close();
    }
}
