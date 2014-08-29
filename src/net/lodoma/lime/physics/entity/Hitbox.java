package net.lodoma.lime.physics.entity;

import java.util.Map;
import java.util.Set;

import net.lodoma.lime.physics.Collider;

public class Hitbox
{
    private Map<String, Collider> colliders;
    
    public Hitbox(Map<String, Collider> colliders)
    {
        this.colliders = colliders;
    }
    
    public Map<String, Collider> getColliders()
    {
        return colliders;
    }
    
    public Collider getCollider(String name)
    {
        return colliders.get(name);
    }
    
    public Set<String> getColliderNames()
    {
        return colliders.keySet();
    }
}
