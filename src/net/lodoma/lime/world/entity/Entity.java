package net.lodoma.lime.world.entity;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.script.library.LimeLibrary;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.world.World;

public class Entity implements Identifiable<Integer>
{
    public int identifier;
    
    public LuaScript script;
    public World world;
    
    public Body body;
    
    public Entity(World world, int hash, Server server)
    {
        try
        {
            EntityType type = world.entityTypePool.get(hash);
            
            script = new LuaScript(new LimeLibrary(server));
            script.load(new File("./script/entity/" + type.script + ".lua"));       // load entity script
        }
        catch (IOException e)
        {
            // TODO: handle this
            e.printStackTrace();
        }
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
    
    public void initialize()
    {
        
    }
    
    public void destroy()
    {
        
    }
    
    public void update(float timeDelta)
    {
        script.call("Lime_Update", new Object[] { identifier, timeDelta, false });
    }
    
    public void debugRender()
    {
        
    }
    
    public void render()
    {
        throw new UnsupportedOperationException();
    }
}
