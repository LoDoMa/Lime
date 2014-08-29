package net.lodoma.lime.physics.entity;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.world.CommonWorld;

public class EntityType
{
    private static int idCounter = 0;
    
    private String name;
    private int nameHash;
    private String version;
    
    private Model model;
    private Hitbox hitbox;
    
    private LuaScript script;
    private CommonWorld world;
    
    public EntityType(String name, String version,
                      Model model, Hitbox hitbox,
                      String scriptFile,
                      CommonWorld world) throws IOException
    {
        this.name = name;
        this.nameHash = HashHelper.hash32(name);
        this.version = version;
        
        this.model = model;
        this.hitbox = hitbox;
        
        this.script = new LuaScript();
        this.world = world;
        
        initLuaState(scriptFile);
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getNameHash()
    {
        return nameHash;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public Model getModel()
    {
        return model;
    }
    
    public Hitbox getHitbox()
    {
        return hitbox;
    }
    
    public CommonWorld getWorld()
    {
        return world;
    }
    
    public Entity newEntity()
    {
        return new Entity(this, idCounter++);
    }
    
    public void update(Entity entity, boolean isActor, double timeDelta)
    {
        int id = entity.getIdentifier();
        Object[] arguments = new Object[] { id, isActor, timeDelta };
        script.call("Lime_EntityUpdate", arguments);
    }
    
    public void render(Entity entity)
    {
        model.render(entity.getModelData());
    }
    
    private void initLuaState(String scriptFile) throws IOException
    {
        script.setGlobal("SCRIPT", script);
        script.setGlobal("ENTITY_TYPE", this);
        script.require("script/strict/lime");
        script.require("script/strict/entity");
        script.require("script/strict/sandbox");
        
        script.load(new File(scriptFile));
    }
}
