package net.lodoma.lime.physics.entity;

import java.util.Map;
import java.util.Set;

import net.lodoma.lime.physics.Collider;

public class Hitbox
{
    private Map<Integer, Collider> colliders;
    
    public Hitbox(Map<Integer, Collider> colliders)
    {
        this.colliders = colliders;
    }
    
    public Map<Integer, Collider> getColliders()
    {
        return colliders;
    }
    
    public Collider getCollider(int hash)
    {
        return colliders.get(hash);
    }
    
    public Set<Integer> getColliderHashes()
    {
        return colliders.keySet();
    }
}
