package net.lodoma.lime.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbox2d.collision.shapes.CircleShape;

import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.entity.Entity;
import net.lodoma.lime.world.entity.EntityShape;
import net.lodoma.lime.world.physics.PhysicsComponent;

public class Snapshot
{
    public boolean isDelta;
    public List<Integer> removedEntities;
    public List<Integer> removedLights;
    public Map<Integer, EntityShape> entityData;
    public Map<Integer, LightData> lightData;
    
    public Snapshot()
    {
        removedEntities = new ArrayList<Integer>();
        removedLights = new ArrayList<Integer>();
        entityData = new HashMap<Integer, EntityShape>();
        lightData = new HashMap<Integer, LightData>();
    }
    
    public Snapshot(World world)
    {
        this();
        isDelta = false;
        
        world.entityPool.foreach((Entity entity) -> {
            int compoc = entity.body.components.size();
            
            EntityShape shape = new EntityShape();
            shape.positionList = new Vector2[compoc];
            shape.angleList = new float[compoc];
            shape.radiusList = new float[compoc];
            
            List<PhysicsComponent> objects = entity.body.components.getObjectList();
            for (int i = 0; i < objects.size(); i++)
            {
                PhysicsComponent component = objects.get(i);
                shape.positionList[i] = new Vector2(component.engineBody.getPosition());
                shape.angleList[i] = component.engineBody.getAngle();
                shape.radiusList[i] = ((CircleShape) component.engineFixture.m_shape).m_radius;
            }
            
            entityData.put(entity.identifier, shape);
        });
        
        world.lightPool.foreach((Light light) -> {
            lightData.put(light.identifier, light.data);
        });
    }
}
