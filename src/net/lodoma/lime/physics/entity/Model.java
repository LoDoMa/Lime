package net.lodoma.lime.physics.entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.mask.Mask;

public class Model
{
    private Map<Integer, Mask> masks;
    
    public Model(Map<Integer, Mask> masks)
    {
        this.masks = masks;
    }
    
    public Map<Integer, Mask> getMasks()
    {
        return masks;
    }
    
    public Mask getMask(int hash)
    {
        return masks.get(hash);
    }
    
    public Set<Integer> getMaskHashes()
    {
        return new HashSet<Integer>(masks.keySet());
    }
    
    public void render(ModelData data)
    {
        Set<Integer> hashes = getMaskHashes();
        for(Integer hash : hashes)
            masks.get(hash).call(data.getTransform(hash));
    }
}
