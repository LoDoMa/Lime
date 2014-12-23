package net.lodoma.lime.script.library;

import net.lodoma.lime.server.Server;

import org.luaj.vm2.LuaTable;

public class LimeLibrary
{
    public LuaTable table;
    public Server server;
    
    public LimeLibrary(Server server)
    {
        table = LuaTable.tableOf();
        this.server = server;
    }
}
