package net.lodoma.lime.physics;

import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.world.CommonWorld;

public class Entity implements Identifiable<Integer>
{
    public int identifier;
    
    public EntityType type;
    public CommonWorld world;
    
    public Body body;
    public Shape shape;
    
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
        world.physicsWorld.bodyPool.add(body);
        if (shape != null)
            world.visualWorld.shapePool.add(shape);
        
        if(type.script != null)
        {
            Object[] arguments = new Object[] { identifier };
            type.script.call("Lime_Initialize", arguments);
        }
    }
    
    public void destroy()
    {
        world.physicsWorld.bodyPool.remove(body);
    }
    
    public void update(float timeDelta)
    {
        if(type.script != null)
        {
            Object[] arguments = new Object[] { identifier, false, timeDelta };
            type.script.call("Lime_Update", arguments);
        }
    }
    
    public void postUpdate(float timeDelta)
    {
        if(type.script != null)
        {
            Object[] arguments = new Object[] { identifier, false, timeDelta };
            type.script.call("Lime_PostUpdate", arguments);
        }
    }
}
