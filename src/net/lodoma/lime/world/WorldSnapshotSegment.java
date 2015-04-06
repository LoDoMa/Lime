package net.lodoma.lime.world;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.snapshot.SnapshotData;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.physics.InvalidPhysicsParticleException;
import net.lodoma.lime.world.physics.PhysicsParticle;
import net.lodoma.lime.world.physics.PhysicsShapeSnapshot;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;
import net.lodoma.lime.world.physics.PhysicsComponentType;
import net.lodoma.lime.world.physics.PhysicsParticleDefinition;

public class WorldSnapshotSegment implements SnapshotData
{
    private static class ModifShape
    {
        public int compoID;
        public List<Integer> shapesMod;
        
        public ModifShape(int compoID)
        {
            this.compoID = compoID;
            shapesMod = new ArrayList<Integer>();
        }
    }
    
    private WorldSnapshot full;
    
    private List<Integer> removedComponents;
    private List<Integer> componentsModTransform;
    private List<ModifShape> componentsModShape;
    private List<Integer> componentsModPhysicsData;

    private List<Integer> removedLights;    
    private List<Integer> lightsMod;
    
    public WorldSnapshotSegment()
    {
        
    }
    
    public WorldSnapshotSegment(WorldSnapshot current, WorldSnapshot previous)
    {
        full = current;

        removedComponents = new ArrayList<Integer>();
        componentsModTransform = new ArrayList<Integer>();
        componentsModShape = new ArrayList<ModifShape>();
        componentsModPhysicsData = new ArrayList<Integer>();
        
        removedLights = new ArrayList<Integer>();
        lightsMod = new ArrayList<Integer>();
        
        Set<Integer> currentComponents = current.componentData.keySet();
        Set<Integer> previousComponents = previous.componentData.keySet();

        for (int currentKey : currentComponents)
        {
            PhysicsComponentSnapshot currentCompo = current.componentData.get(currentKey);
            if (!previousComponents.contains(currentKey))
            {
                ModifShape modif = new ModifShape(currentKey);
                for (int i = 0; i < currentCompo.shapes.length; i++)
                    modif.shapesMod.add(i);
                
                componentsModTransform.add(currentKey);
                componentsModShape.add(modif);
                componentsModPhysicsData.add(currentKey);
            }
            else
            {
                PhysicsComponentSnapshot previousCompo = previous.componentData.get(currentKey);
                
                if (!Vector2.equals(currentCompo.position, previousCompo.position) ||
                    currentCompo.angle != previousCompo.angle)
                    componentsModTransform.add(currentKey);
                if (currentCompo.shapes.length != previousCompo.shapes.length)
                {
                    ModifShape modif = new ModifShape(currentKey);
                    for (int i = 0; i < currentCompo.shapes.length; i++)
                        modif.shapesMod.add(i);
                    componentsModShape.add(modif);
                }
                else
                {
                    ModifShape modif = new ModifShape(currentKey);
                    for (int i = 0; i < currentCompo.shapes.length; i++)
                        if (!currentCompo.shapes[i].compare(previousCompo.shapes[i]))
                            modif.shapesMod.add(i);
                    if (modif.shapesMod.size() > 0)
                        componentsModShape.add(modif);
                }
                if (currentCompo.type != previousCompo.type)
                    componentsModPhysicsData.add(currentKey);
            }
        }
        
        for (int previousKey : previousComponents)
            if (!currentComponents.contains(previousKey))
                removedComponents.add(previousKey);
        
        Set<Integer> currentLights = current.lightData.keySet();
        Set<Integer> previousLights = previous.lightData.keySet();

        for (int currentKey : currentLights)
        {
            LightData currentLight = current.lightData.get(currentKey);
            if (!previousLights.contains(currentKey))
            {
                lightsMod.add(currentKey);
            }
            else
            {
                LightData previousLight = previous.lightData.get(currentKey);
                
                if (!Vector2.equals(currentLight.position, previousLight.position) ||
                    currentLight.radius != previousLight.radius ||
                    !currentLight.color.equals(previousLight.color))
                    lightsMod.add(currentKey);
            }
        }
        
        for (int previousKey : previousLights)
            if (!currentLights.contains(previousKey))
                removedLights.add(previousKey);
    }
    
    @Override
    public void read(Client client, DataInputStream in) throws IOException
    {
        World world = client.world;
        synchronized (world.lock)
        {
            int nRemovedComponents = in.readInt();
            int nComponentsModTransform = in.readInt();
            int nComponentsModShape = in.readInt();
            int nComponentsModPhysicsData = in.readInt();
    
            int nRemovedLights = in.readInt();
            int nLightsMod = in.readInt();
            
            int nParticle = in.readInt();

            client.worldRenderer.camera.translation.x = in.readFloat();
            client.worldRenderer.camera.translation.y = in.readFloat();
            client.worldRenderer.camera.rotation = in.readFloat();
            client.worldRenderer.camera.scale.x = in.readFloat();
            client.worldRenderer.camera.scale.y = in.readFloat();

            world.lightAmbientColor.r = in.readFloat();
            world.lightAmbientColor.g = in.readFloat();
            world.lightAmbientColor.b = in.readFloat();
            world.lightAmbientColor.a = in.readFloat();
            
            for (int c = 0; c < nRemovedComponents; c++)
            {
                PhysicsComponentSnapshot compo = world.compoSnapshotPool.get(in.readInt());
                if (compo.type == PhysicsComponentType.STATIC)
                {
                    world.componentPool.get(compo.identifier).destroy();
                    world.componentPool.remove(compo.identifier);
                }
                world.compoSnapshotPool.remove(compo.identifier);
            }
            for (int c = 0; c < nComponentsModTransform; c++)
            {
                int key = in.readInt();
                if (!world.compoSnapshotPool.has(key))
                {
                    PhysicsComponentSnapshot compo = new PhysicsComponentSnapshot();
                    compo.identifier = key;
                    world.compoSnapshotPool.addManaged(compo);
                }
                PhysicsComponentSnapshot compo = world.compoSnapshotPool.get(key);
                compo.position = new Vector2();
                compo.position.x = in.readFloat();
                compo.position.y = in.readFloat();
                compo.angle = in.readFloat();
            }
            for (int c = 0; c < nComponentsModShape; c++)
            {
                int key = in.readInt();
                PhysicsComponentSnapshot compo = world.compoSnapshotPool.get(key);
                int nShape = in.readInt();
                if (compo.shapes == null)
                    compo.shapes = new PhysicsShapeSnapshot[nShape];
                else if (compo.shapes.length != nShape)
                {
                    for (PhysicsShapeSnapshot shape : compo.shapes)
                        shape.destroy();
                    compo.shapes = new PhysicsShapeSnapshot[nShape];
                }
                for (int cc = 0; cc < nShape; cc++)
                {
                    int shapeIndex = in.readInt();
                    PhysicsShapeSnapshot shape = compo.shapes[shapeIndex];
                    if (shape == null)
                    {
                        shape = new PhysicsShapeSnapshot();
                        compo.shapes[shapeIndex] = shape;
                    }
                    shape.read(in);
                }
            }
            for (int c = 0; c < nComponentsModPhysicsData; c++)
            {
                int key = in.readInt();
                PhysicsComponentSnapshot compo = world.compoSnapshotPool.get(key);
                compo.type = PhysicsComponentType.values()[in.readInt()];
            }
            
            for (int c = 0; c < nRemovedLights; c++)
            {
                int key = in.readInt();
                world.lightPool.get(key).destroy();
                world.lightPool.remove(key);
            }
            for (int c = 0; c < nLightsMod; c++)
            {
                int key = in.readInt();
                if (!world.lightPool.has(key))
                {
                    Light light = new Light(world);
                    light.identifier = key;
                    world.lightPool.addManaged(light);
                }
                Light light = world.lightPool.get(key);
                light.data.position.x = in.readFloat();
                light.data.position.y = in.readFloat();
                light.data.radius = in.readFloat();
                light.data.color.r = in.readFloat();
                light.data.color.g = in.readFloat();
                light.data.color.b = in.readFloat();
                light.data.color.a = in.readFloat();
            }
            
            for (int c = 0; c < nParticle; c++)
            {
                PhysicsParticleDefinition particleDef = new PhysicsParticleDefinition();
                particleDef.position.x = in.readFloat();
                particleDef.position.y = in.readFloat();
                particleDef.angle = in.readFloat();
                particleDef.angularVelocity = in.readFloat();
                particleDef.linearVelocity.x = in.readFloat();
                particleDef.linearVelocity.y = in.readFloat();

                particleDef.size = in.readFloat();
                particleDef.density = in.readFloat();
                particleDef.restitution = in.readFloat();
                
                particleDef.angularDamping = in.readFloat();
                particleDef.linearDamping = in.readFloat();
                
                particleDef.lifetime = in.readFloat();
                particleDef.destroyOnCollision = in.readBoolean();
                
                try
                {
                    particleDef.validate();
                }
                catch(InvalidPhysicsParticleException e)
                {
                    Lime.LOGGER.C("Validation failed for world particle definition");
                    Lime.LOGGER.log(e);
                    Lime.forceExit(e);
                }
                
                particleDef.create();
                
                world.particleList.add(new PhysicsParticle(particleDef, world.physicsWorld));
            }
        }
    }
    
    @Override
    public void write(Server server, ServerUser user) throws IOException
    {
        user.outputStream.writeInt(removedComponents.size());
        user.outputStream.writeInt(componentsModTransform.size());
        user.outputStream.writeInt(componentsModShape.size());
        user.outputStream.writeInt(componentsModPhysicsData.size());

        user.outputStream.writeInt(removedLights.size());
        user.outputStream.writeInt(lightsMod.size());
        
        user.outputStream.writeInt(full.particleData.size());
        
        user.outputStream.writeFloat(user.camera.translation.x);
        user.outputStream.writeFloat(user.camera.translation.y);
        user.outputStream.writeFloat(user.camera.rotation);
        user.outputStream.writeFloat(user.camera.scale.x);
        user.outputStream.writeFloat(user.camera.scale.y);

        user.outputStream.writeFloat(full.lightAmbientColor.r);
        user.outputStream.writeFloat(full.lightAmbientColor.g);
        user.outputStream.writeFloat(full.lightAmbientColor.b);
        user.outputStream.writeFloat(full.lightAmbientColor.a);

        for (int key : removedComponents) user.outputStream.writeInt(key);
        for (int key : componentsModTransform)
        {
            PhysicsComponentSnapshot compo = full.componentData.get(key);
            user.outputStream.writeInt(key);
            user.outputStream.writeFloat(compo.position.x);
            user.outputStream.writeFloat(compo.position.y);
            user.outputStream.writeFloat(compo.angle);
        }
        for (ModifShape modif : componentsModShape)
        {
            PhysicsComponentSnapshot compo = full.componentData.get(modif.compoID);
            user.outputStream.writeInt(modif.compoID);
            user.outputStream.writeInt(modif.shapesMod.size());
            for (int i = 0; i < modif.shapesMod.size(); i++)
            {
                int shapeIndex = modif.shapesMod.get(i);
                PhysicsShapeSnapshot shape = compo.shapes[shapeIndex];
                user.outputStream.writeInt(shapeIndex);
                shape.write(user.outputStream);
            }
        }
        for (int key : componentsModPhysicsData)
        {
            PhysicsComponentSnapshot compo = full.componentData.get(key);
            user.outputStream.writeInt(key);
            user.outputStream.writeInt(compo.type.ordinal());
        }

        for (int key : removedLights) user.outputStream.writeInt(key);
        for (int key : lightsMod)
        {
            LightData light = full.lightData.get(key);
            user.outputStream.writeInt(key);
            user.outputStream.writeFloat(light.position.x);
            user.outputStream.writeFloat(light.position.y);
            user.outputStream.writeFloat(light.radius);
            user.outputStream.writeFloat(light.color.r);
            user.outputStream.writeFloat(light.color.g);
            user.outputStream.writeFloat(light.color.b);
            user.outputStream.writeFloat(light.color.a);
        }
        
        for (PhysicsParticleDefinition particleDef : full.particleData)
        {
            user.outputStream.writeFloat(particleDef.position.x);
            user.outputStream.writeFloat(particleDef.position.y);
            user.outputStream.writeFloat(particleDef.angle);
            user.outputStream.writeFloat(particleDef.angularVelocity);
            user.outputStream.writeFloat(particleDef.linearVelocity.x);
            user.outputStream.writeFloat(particleDef.linearVelocity.y);

            user.outputStream.writeFloat(particleDef.size);
            user.outputStream.writeFloat(particleDef.density);
            user.outputStream.writeFloat(particleDef.restitution);

            user.outputStream.writeFloat(particleDef.angularDamping);
            user.outputStream.writeFloat(particleDef.linearDamping);

            user.outputStream.writeFloat(particleDef.lifetime);
            user.outputStream.writeBoolean(particleDef.destroyOnCollision);
        }
    }
}
