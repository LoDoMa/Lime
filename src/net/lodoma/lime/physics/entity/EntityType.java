package net.lodoma.lime.physics.entity;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.script.LuaScript;

public class EntityType
{
    private String name;
    private String version;
    
    private final Model model;
    private final Hitbox hitbox;
    
    private final LuaScript script;
    
    public EntityType(String name, String version,
                      Model model, Hitbox hitbox,
                      String scriptFile) throws IOException
    {
        this.name = name;
        this.version = version;
        
        this.model = model;
        this.hitbox = hitbox;
        
        this.script = new LuaScript();
        initLuaState(scriptFile);
    }
    
    private void initLuaState(String scriptFile) throws IOException
    {
        script.setGlobal("SCRIPT", script);
        script.require("script/strict/lime");
        script.require("script/strict/entity");
        script.require("script/strict/sandbox");
        
        script.load(new File(scriptFile));
    }
}
