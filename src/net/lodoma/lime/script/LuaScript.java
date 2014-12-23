package net.lodoma.lime.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.lodoma.lime.script.library.LimeLibrary;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaScript
{
	public Globals globals = JsePlatform.standardGlobals();
	
	// Globals must be created for each thread.
	// Since it is not static here, LuaScript is thread safe,
	// but we might want to make it static for optimization later.
	
	public LuaScript(LimeLibrary library)
	{
		globals.set("lime", library.table);
	}
	
    public static boolean safeType(Object object) { return true; }
	
	public void setGlobal(String name, Object value)
	{
		LuaValue luaValue = CoerceJavaToLua.coerce(value);
		globals.set(name, luaValue);
	}
	
    public void require(String file)
    {
    	load("require \"" + file + "\"", "LuaScript.require");
    }
    
    public void load(File file) throws IOException
    {
    	String source = "";
    	
    	// Move this to some util class
    	BufferedReader reader = new BufferedReader(new FileReader(file));
    	String line;
    	while ((line = reader.readLine()) != null)
    		source += line + "\n";
    	reader.close();
    	
    	load(source, file.getPath());
    }
    
	public void load(String source, String chunkname)
	{
		LuaValue chunk = globals.load(source, chunkname);
		chunk.call();
	}
	
    public void call(String functionPath, Object[] arguments)
    {
    	if(arguments == null)
            arguments = new Object[0];
    	
    	String[] segm = functionPath.split("\\.");
    	
    	LuaTable table = globals;
    	for (int i = 0; i < segm.length - 1; i++)
    	{
    		LuaValue newTable = table.get(segm[i]);
    		table = newTable.checktable();
    	}
    	
    	LuaValue functionValue = table.get(segm[segm.length - 1]);
    	LuaFunction function = functionValue.checkfunction();
    	
    	LuaValue[] luaArguments = new LuaValue[arguments.length];
    	for (int i = 0; i < arguments.length; i++)
    		luaArguments[i] = CoerceJavaToLua.coerce(arguments);
    	
    	Varargs returnedVarargs = function.invoke(luaArguments);
    	if (returnedVarargs.narg() != 0)
    		throw new LuaError("function \"" + functionPath + "\" may not return anything");
    }
}
