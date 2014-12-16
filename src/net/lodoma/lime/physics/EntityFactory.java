package net.lodoma.lime.physics;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.IdentityPool;
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
    
    public static class ShapeConfiguration
    {
        public List<ShapeComponentConfiguration> shapeComponentConfigList;
        
        public Shape newShape()
        {
            Shape shape = new Shape();
            shape.componentPool = new IdentityPool<ShapeComponent>(true);
            for (int i = 0; i < shapeComponentConfigList.size(); i++)
            {
                ShapeComponentConfiguration shapeComponentConfig = shapeComponentConfigList.get(i);
                ShapeComponent shapeComponent = shapeComponentConfig.newShapeComponent();
                shapeComponent.shape = shape;
                shape.componentPool.add(shapeComponent);
            }
            return shape;
        }
    }
    
    public static class ShapeComponentConfiguration
    {
        public int identifier;
        public ShapeComponent.ComponentType type;
        public float radius;
        
        public ShapeComponent newShapeComponent()
        {
            ShapeComponent shapeComponent = new ShapeComponent();
            shapeComponent.identifier = identifier;
            shapeComponent.position = new Vector2(0.0f, 0.0f);
            shapeComponent.rotation = 0.0f;
            switch (type)
            {
            case CIRCLE:
                shapeComponent.radius = radius;
                break;
            }
            return shapeComponent;
        }
    }

    public String name;
    public int nameHash;
    public String version;
    public PhysicsConfiguration physicsConfig;
    public ShapeConfiguration shapeConfig;
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
            type.nameHash = nameHash;
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
        entity.world = world;
        
        if (world.physicsWorld != null)
        {
            entity.body = physicsConfig.newBody();
            entity.body.world = world.physicsWorld;
        }
        else
            throw new EntityFactoryException("attempt to create Entity in a world without PhysicsWorld");
        
        if (world.visualWorld != null && shapeConfig != null)
        {
            entity.shape = shapeConfig.newShape();
            entity.shape.world = world.visualWorld;
        }
        
        return entity;
    }
}
