package net.lodoma.lime.physics.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.util.Transform;
import net.lodoma.lime.util.Vector2;

public class HitboxData
{
    private Map<Integer, Transform> transformations;
    
    public HitboxData(Hitbox hitbox)
    {
        transformations = new HashMap<Integer, Transform>();
        
        Set<Integer> hashes = hitbox.getColliderHashes();
        for(Integer hash : hashes)
            transformations.put(hash, new Transform(new Vector2(0.0f, 0.0f), 0.0f));
    }
    
    public Transform getTransform(int hash)
    {
        return transformations.get(hash);
    }
}
