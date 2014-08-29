package net.lodoma.lime.physics.entity;

import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.mask.Mask;

public class EntityData
{
    public int nameHash;
    public String name;
    public String version;
    
    public Map<Integer, Mask> masks;
    public String script;
    
    public EntityData()
    {
        masks = new HashMap<Integer, Mask>();
    }
}
