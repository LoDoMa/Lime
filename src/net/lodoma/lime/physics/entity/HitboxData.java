package net.lodoma.lime.physics.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.physics.PhysicsObject;
import net.lodoma.lime.util.Transform;
import net.lodoma.lime.util.Vector2;

public class HitboxData
{
    private static class DataBundle
    {
        public Transform transform;
        public Vector2 velocity;
        public PhysicsObject physicsObject;
    }
    
    private Map<Integer, DataBundle> transformations;
    
    public HitboxData(Hitbox hitbox)
    {
        transformations = new HashMap<Integer, DataBundle>();
        
        Set<Integer> hashes = hitbox.getColliderHashes();
        for(Integer hash : hashes)
        {
            DataBundle bundle = new DataBundle();
            bundle.transform = new Transform(new Vector2(0.0f, 0.0f), 0.0f);
            bundle.velocity = new Vector2(0.0f, 0.0f);
            bundle.physicsObject = new PhysicsObject(bundle.transform, bundle.velocity, hitbox.getCollider(hash));
            transformations.put(hash, bundle);
        }
    }
    
    public Transform getTransform(int hash)
    {
        return transformations.get(hash).transform;
    }
    
    public Vector2 getVelocity(int hash)
    {
        return transformations.get(hash).velocity;
    }
    
    public PhysicsObject getPhysicsObject(int hash)
    {
        return transformations.get(hash).physicsObject;
    }
}
