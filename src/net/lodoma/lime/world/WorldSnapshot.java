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
import net.lodoma.lime.world.physics.PhysicsParticleDefinition;

public class WorldSnapshot
{
    public Map<Integer, PhysicsComponentSnapshot> componentData;
    public Map<Integer, LightData> lightData;
    public List<PhysicsParticleDefinition> particleData;
    
    public WorldSnapshot()
    {
        componentData = new HashMap<Integer, PhysicsComponentSnapshot>();
        lightData = new HashMap<Integer, LightData>();
        particleData = new ArrayList<PhysicsParticleDefinition>();
    }
    
    public WorldSnapshot(Server server)
    {
        this();
        
        server.world.componentPool.foreach((PhysicsComponent compo) -> {
            componentData.put(compo.identifier, compo.createSnapshot());
        });
        
        server.world.lightPool.foreach((Light light) -> {
            lightData.put(light.identifier, light.data);
        });
        
        server.world.particleDefinitionList.forEach((PhysicsParticleDefinition particleDef) -> {
            particleData.add(particleDef);
        });
    }
}
