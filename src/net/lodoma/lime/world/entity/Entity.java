package net.lodoma.lime.world.entity;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.script.library.AttributeFunctions;
import net.lodoma.lime.script.library.EntityFunctions;
import net.lodoma.lime.script.library.EventFunctions;
import net.lodoma.lime.script.library.LimeLibrary;
import net.lodoma.lime.script.library.UtilFunctions;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.world.World;

public class Entity implements Identifiable<Integer>
{
    public int identifier;
    
    public LuaScript script;
    public World world;
    
    public EntityBody body;
    public EntityShape shape;
    
    public AttributeMap attributes;
    
    public Entity(World world, Server server)
    {
        this.world = world;
        body = new EntityBody();
        attributes = new AttributeMap();
    }
    
    public Entity(World world, int identifier, Client client)
    {
        this.world = world;
        this.identifier = identifier;
        
        shape = new EntityShape();
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
        LimeLibrary library = new LimeLibrary(server);
        UtilFunctions.addToLibrary(library);
        EntityFunctions.addToLibrary(library);
        EventFunctions.addToLibrary(library);
        AttributeFunctions.addToLibrary(library);
        
        script = new LuaScript(library);
        
        try
        {
            script.load(new File("./script/entity/" + scriptName + ".lua"));
        }
        catch (IOException e)
        {
            // TODO: handle this
            e.printStackTrace();
        }
        
        init();
    }
    
    public void init()
    {
        script.call("Lime_Init", new Object[] { identifier });
    }
    
    public void update(double timeDelta)
    {
        script.call("Lime_Update", new Object[] { identifier, timeDelta, false });
    }
    
    public void destroy()
    {
        if (body != null)
            body.destroy();
        if (script != null)
            script.call("Lime_Clean", new Object[] { identifier });
    }
    
    public void debugRender()
    {
        shape.debugRender();
    }
    
    public void render()
    {
        shape.tempRender();
    }
}
