package net.lodoma.lime.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.world.entity.Entity;
import net.lodoma.lime.world.entity.EntityShape;
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class Snapshot
{
    public boolean isDelta;
    public List<Integer> removedTerrain;
    public List<Integer> removedEntities;
    public List<Integer> removedLights;
    public Map<Integer, PhysicsComponentSnapshot> terrainData;
    public Map<Integer, EntityShape> entityData;
    public Map<Integer, LightData> lightData;
    
    public Snapshot()
    {
        removedTerrain = new ArrayList<Integer>();
        removedEntities = new ArrayList<Integer>();
        removedLights = new ArrayList<Integer>();
        terrainData = new HashMap<Integer, PhysicsComponentSnapshot>();
        entityData = new HashMap<Integer, EntityShape>();
        lightData = new HashMap<Integer, LightData>();
    }
    
    public Snapshot(World world)
    {
        this();
        isDelta = false;
        
        world.terrain.physicalComponents.foreach((PhysicsComponent compo) -> {
            terrainData.put(compo.identifier, compo.createSnapshot());
        });
        
        world.entityPool.foreach((Entity entity) -> {
            int compoc = entity.body.components.size();
            
            EntityShape shape = new EntityShape();
            shape.snapshots = new PhysicsComponentSnapshot[compoc];
            
            List<PhysicsComponent> objects = entity.body.components.getObjectList();
            for (int i = 0; i < objects.size(); i++)
            {
                PhysicsComponent component = objects.get(i);
                PhysicsComponentSnapshot compoSnapshot = component.createSnapshot();
                shape.snapshots[i] = compoSnapshot;
            }
            
            entityData.put(entity.identifier, shape);
        });
        
        world.lightPool.foreach((Light light) -> {
            lightData.put(light.identifier, light.data);
        });
    }
}
