package net.lodoma.lime.world.entity;

import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.world.World;

public class Entity implements Identifiable<Integer>
{
    public int identifier;
    
    public LuaScript script;
    public World world;
    
    public Body body;
    
    public Entity(int hash)
    {
        
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
        
    }
    
    public void debugRender()
    {
        
    }
    
    public void render()
    {
        throw new UnsupportedOperationException();
    }
}
