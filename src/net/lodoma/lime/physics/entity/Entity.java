package net.lodoma.lime.physics.entity;

import java.util.Set;

import net.lodoma.lime.common.PropertyPool;
import net.lodoma.lime.physics.PhysicsWorld;
import net.lodoma.lime.util.Identifiable;

public class Entity implements Identifiable<Integer>
{
    private EntityType type;
    private int ID;
    
    private HitboxData hitboxData;
    private ModelData modelData;
    
    private PropertyPool propertyPool;
    
    private boolean initialized;
    
    public Entity(EntityType type, int id)
    {
        this.type = type;
        this.ID = id;

        this.hitboxData = new HitboxData(type.getHitbox());
        this.modelData = new ModelData(type.getModel());
        
        this.propertyPool = type.getWorld().getPropertyPool();
        
        this.initialized = false;
        
        PhysicsWorld physicsWorld = type.getWorld().getPhysicsWorld();
        Set<Integer> colliders = type.getHitbox().getColliderHashes();
        for(int collider : colliders)
            physicsWorld.addPhysicsObject(hitboxData.getPhysicsObject(collider));
    }
    
    public void destroy()
    {
        
    }
    
    public EntityType getType()
    {
        return type;
    }
    
    @Override
    public Integer getIdentifier()
    {
        return ID;
    }
    
    public HitboxData getHitboxData()
    {
        return hitboxData;
    }
    
    public ModelData getModelData()
    {
        return modelData;
    }
    
    public void update(double timeDelta)
    {
        if(!initialized)
        {
            type.initialize(this);
            initialized = true;
        }
        
        boolean isActor = isActor();
        type.update(this, isActor, timeDelta);
    }
    
    public void render()
    {
        type.render(this);
    }
    
    private boolean isActor()
    {
        if(propertyPool.hasProperty("actor"))
            return ((Integer) propertyPool.getProperty("actor")) == ID;
        return false;
    }
}
