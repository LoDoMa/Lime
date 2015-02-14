package net.lodoma.lime.world.entity;

import java.io.File;
import java.io.IOException;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.OsHelper;
import net.lodoma.lime.world.World;

public class Entity implements Identifiable<Integer>
{
    public int identifier;

    public LuaFunction scriptInit;
    public LuaFunction scriptUpdate;
    public LuaFunction scriptClean;
    
    public World world;
    
    public AttributeMap attributes;
    
    public Entity(World world, Server server)
    {
        this.world = world;
        attributes = new AttributeMap();
    }
    
    public Entity(World world, int identifier, Client client)
    {
        this.world = world;
        this.identifier = identifier;
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public void assignScript(Server server, String scriptName)
    {
        File scriptFile = new File(OsHelper.JARPATH + "script/entity/" + scriptName + ".lua");
        try
        {
            world.luaInstance.load(scriptFile);
        }
        catch (IOException e)
        {
            Lime.LOGGER.C("Failed to load the gamemode from file " + scriptFile);
            Lime.LOGGER.log(e);
            Lime.forceExit();
        }
        
        scriptInit = world.luaInstance.globals.get("Lime_Init").checkfunction();
        scriptUpdate = world.luaInstance.globals.get("Lime_Update").checkfunction();
        scriptClean = world.luaInstance.globals.get("Lime_Clean").checkfunction();
        
        world.luaInstance.globals.set("Lime_Init", LuaValue.NIL);
        world.luaInstance.globals.set("Lime_Update", LuaValue.NIL);
        world.luaInstance.globals.set("Lime_Clean", LuaValue.NIL);

        Lime.LOGGER.I("Assigned script \"" + scriptName + "\" to entity " + identifier);
        
        init();
    }
    
    public void init()
    {
        Lime.LOGGER.I("Initializing script for entity " + identifier);
        world.luaInstance.call(scriptInit, new Object[] { identifier });
    }
    
    public void update(double timeDelta)
    {
        world.luaInstance.call(scriptUpdate, new Object[] { identifier, timeDelta });
    }
    
    public void destroy()
    {
        Lime.LOGGER.I("Destroying script for entity " + identifier);
        world.luaInstance.call(scriptClean, new Object[] { identifier });
    }
}
