package net.lodoma.lime.physics.entity;

import java.util.Map;
import java.util.Set;

import net.lodoma.lime.mask.Mask;

public class Model
{
    private Map<String, Mask> masks;
    
    public Model(Map<String, Mask> masks)
    {
        this.masks = masks;
    }
    
    public Map<String, Mask> getMasks()
    {
        return masks;
    }
    
    public Mask getMask(String name)
    {
        return masks.get(name);
    }
    
    public Set<String> getMaskNames()
    {
        return masks.keySet();
    }
}
