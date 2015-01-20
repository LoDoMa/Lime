package net.lodoma.lime.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class Snapshot
{
    public boolean isDelta;
    public List<Integer> removedComponents;
    public List<Integer> removedLights;
    public Map<Integer, PhysicsComponentSnapshot> componentData;
    public Map<Integer, LightData> lightData;
    
    public Snapshot()
    {
        removedComponents = new ArrayList<Integer>();
        removedLights = new ArrayList<Integer>();
        componentData = new HashMap<Integer, PhysicsComponentSnapshot>();
        lightData = new HashMap<Integer, LightData>();
    }
    
    public Snapshot(Server server)
    {
        this();
        isDelta = false;
        
        server.world.componentPool.foreach((PhysicsComponent compo) -> {
            componentData.put(compo.identifier, compo.createSnapshot());
        });
        
        server.world.lightPool.foreach((Light light) -> {
            lightData.put(light.identifier, light.data);
        });
    }
}
