package net.lodoma.lime.script;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.VarArgFunction;

public class LimeLibrary
{
	public static final int TEST = 0;
	
	public LuaTable table;
	
	public LimeLibrary()
	{
		table = LuaValue.tableOf();
		new LimeLibraryFunc(TEST, "test").addToLibrary();
	}
	
	public class LimeLibraryFunc extends VarArgFunction
	{
		private int opcode;
		private String name;
		
		public LimeLibraryFunc(int opcode, String name)
		{
			this.opcode = opcode;
			this.name = name;
		}
		
		public void addToLibrary()
		{
			table.set(name, this);
		}
		
		@Override
		public LuaValue call()
		{
			switch (opcode)
			{
			case TEST:
			{
				System.out.println("hello!");
				break;
			}
			}
			return LuaValue.NONE;
		}
	}
}
