package net.lodoma.lime.physics;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.CommonWorld;

public class EntityFactory
{
    public static class PhysicsConfiguration
    {
        public float restitution;
        public float density;
        public BodyConfiguration bodyConfig;
        
        public Body newBody()
        {
            Body body = new Body();
            
            body.position = new Vector2(0.0f, 0.0f);
            body.velocity = new Vector2(0.0f, 0.0f);
            body.restitution = restitution;
            body.density = density;
            bodyConfig.fillBody(body);
            body.mass = density * body.volume;
            
            return body;
        }
    }
    
    public static class BodyConfiguration
    {
        public Body.BodyType type;
        public float radius;
        
        public void fillBody(Body body)
        {
            switch (type)
            {
            case CIRCLE:
                body.radius = radius;
                body.volume = radius * radius * (float) Math.PI;
                break;
            }
        }
    }

    public String name;
    public int nameHash;
    public String version;
    public PhysicsConfiguration physicsConfig;
    public boolean actorCapability;
    public String script;

    public CommonWorld world;
    public EntityType type;
    
    public void createEntityType() throws EntityFactoryException
    {
        try
        {
            if (type != null)
                throw new EntityFactoryException("attempt to recreate EntityType for " + name);
            type = new EntityType();
            
            type.name = name;
            type.version = version;
            type.actorCapability = actorCapability;
            if (!script.equals("-NONE"))
            {
                type.script = new LuaScript();
                type.script.setGlobal("SCRIPT", script);
                type.script.setGlobal("ENTITY_FACTORY", this);
                type.script.require("script/strict/lime");
                type.script.require("script/strict/entity");
                type.script.require("script/strict/sandbox");
                type.script.load(new File(script));
            }
        }
        catch (IOException e)
        {
            throw new EntityFactoryException(e);
        }
    }
    
    public void destroy()
    {
        
    }
    
    public Entity newEntity() throws EntityFactoryException
    {
        if (type == null)
            throw new EntityFactoryException("attempt to create Entity before EntityType for " + name);
        
        Entity entity = new Entity();
        entity.type = type;
        entity.body = physicsConfig.newBody();
        
        entity.world = world;
        entity.body.world = world.physicsWorld;
        
        return entity;
    }
}
