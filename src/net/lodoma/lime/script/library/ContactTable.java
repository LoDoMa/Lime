package net.lodoma.lime.script.library;

import org.jbox2d.dynamics.contacts.Contact;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class ContactTable
{
    public static synchronized LuaTable create(Contact contact)
    {
        return new ContactTable(contact).table;
    }

    private LuaTable table;
    private Contact contact;
    
    public ContactTable(Contact contact)
    {
        table = LuaTable.tableOf();
        this.contact = contact;
        
        for (FuncData func : FuncData.values())
            new LimeFunc(func).addToTable();
    }
    
    private class LimeFunc extends VarArgFunction
    {
        private FuncData data;
        
        public LimeFunc(FuncData data)
        {
            this.data = data;
        }
        
        public void addToTable()
        {
            table.set(data.name, this);
        }
        
        @Override
        public Varargs invoke(Varargs args)
        {
            if ((data.argcexact && args.narg() != data.argc) || (!data.argcexact && args.narg() < data.argc))
                throw new LuaError("invalid argument count to Lime function \"" + data.name + "\"");
            
            switch (data)
            {
            case IS_ENABLED:
            {
                return LuaValue.valueOf(contact.isEnabled());
            }
            case SET_ENABLED:
            {
                boolean enabled = args.arg(1).checkboolean();
                contact.setEnabled(enabled);
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        IS_ENABLED(0, true, "isEnabled"),
        SET_ENABLED(1, true, "setEnabled");
        
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
