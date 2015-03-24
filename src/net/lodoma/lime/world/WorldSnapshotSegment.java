package net.lodoma.lime.world;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.Client;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.shader.light.LightModifications;
import net.lodoma.lime.snapshot.SnapshotData;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.physics.InvalidPhysicsParticleException;
import net.lodoma.lime.world.physics.PhysicsComponentModifications;
import net.lodoma.lime.world.physics.PhysicsComponentShapeType;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;
import net.lodoma.lime.world.physics.PhysicsComponentType;
import net.lodoma.lime.world.physics.PhysicsParticleDefinition;

public class WorldSnapshotSegment implements SnapshotData
{
    public static final long MODIFIED_ALL              = 0xFFFFFFFFl << 32;
    public static final long MODIFIED_POSITION         = 0x1l << 32;
    public static final long MODIFIED_ROTATION         = 0x2l << 32;
    public static final long MODIFIED_SHAPE            = 0x4l << 32;
    public static final long MODIFIED_PHYSICS_DATA     = 0x8l << 32;
    
    public int[] createdComponents;
    public int[] removedComponents;
    public long[] modifiedComponents;
    public PhysicsComponentModifications[] productComponents;
    
    public int[] createdLights;
    public int[] removedLights;
    public long[] modifiedLights;
    public LightModifications[] productLights;
    
    public PhysicsParticleDefinition[] createdParticles;
    
    public Color lightAmbientColor;
    
    public WorldSnapshot full;
    
    public WorldSnapshotSegment()
    {
        
    }
    
    public WorldSnapshotSegment(WorldSnapshot current, WorldSnapshot previous)
    {
        Set<Integer> currentComponentKeys = current.componentData.keySet();
        Set<Integer> previousComponentKeys = previous.componentData.keySet();
        List<Integer> createdComponentsList = new ArrayList<Integer>();
        List<Integer> removedComponentsList = new ArrayList<Integer>();
        List<Long> modifiedComponentsList = new ArrayList<Long>();
        
        Set<Integer> currentLightKeys = current.lightData.keySet();
        Set<Integer> previousLightKeys = previous.lightData.keySet();
        List<Integer> createdLightsList = new ArrayList<Integer>();
        List<Integer> removedLightsList = new ArrayList<Integer>();
        List<Long> modifiedLightsList = new ArrayList<Long>();

        for (Integer key : currentComponentKeys)
            if (!previousComponentKeys.contains(key))
            {
                createdComponentsList.add(key);
                modifiedComponentsList.add(MODIFIED_ALL | key);
            }
            else
            {
                PhysicsComponentSnapshot currentCompo = current.componentData.get(key);
                PhysicsComponentSnapshot previousCompo = previous.componentData.get(key);
                
                long modified = 0;
                
                if (!Vector2.equals(currentCompo.position, previousCompo.position))
                    modified |= MODIFIED_POSITION;
                if (currentCompo.angle != previousCompo.angle)
                    modified |= MODIFIED_ROTATION;
                
                if (!currentCompo.shapeType.equals(previousCompo.shapeType))
                    modified |= MODIFIED_SHAPE;
                else switch (currentCompo.shapeType)
                {
                case CIRCLE:
                    if (currentCompo.radius != previousCompo.radius)
                        modified |= MODIFIED_SHAPE;
                    break;
                case POLYGON: case TRIANGLE_GROUP:
                    if (!Arrays.equals(currentCompo.vertices, previousCompo.vertices))
                        modified |= MODIFIED_SHAPE;
                    break;
                }
                
                if (currentCompo.type != previousCompo.type)
                    modified |= MODIFIED_PHYSICS_DATA;
                
                if (modified != 0)
                    modifiedComponentsList.add(modified | key);
            }
        
        for (Integer key : previousComponentKeys)
            if (!currentComponentKeys.contains(key))
                removedComponentsList.add(key);
        
        for (Integer key : currentLightKeys)
            if (!previousLightKeys.contains(key))
            {
                createdLightsList.add(key);
                modifiedLightsList.add(MODIFIED_ALL | key);
            }
            else
            {
                LightData currentLight = current.lightData.get(key);
                LightData previousLight = previous.lightData.get(key);
                
                long modified = 0;

                if (!Vector2.equals(currentLight.position, previousLight.position))
                    modified |= MODIFIED_POSITION;
                if (currentLight.radius != previousLight.radius)
                    modified |= MODIFIED_SHAPE;
                else if (!currentLight.color.equals(previousLight.color))
                    modified |= MODIFIED_SHAPE;
                
                if (modified != 0)
                    modifiedLightsList.add(modified | key);
            }
        
        for (Integer key : previousLightKeys)
            if (!currentLightKeys.contains(key))
                removedLightsList.add(key);
        
        full = current;
        
        createdComponents = new int[createdComponentsList.size()];
        removedComponents = new int[removedComponentsList.size()];
        modifiedComponents = new long[modifiedComponentsList.size()];
        
        for (int i = 0; i < createdComponents.length; i++)
            createdComponents[i] = createdComponentsList.get(i);
        for (int i = 0; i < removedComponents.length; i++)
            removedComponents[i] = removedComponentsList.get(i);
        for (int i = 0; i < modifiedComponents.length; i++)
            modifiedComponents[i] = modifiedComponentsList.get(i);
        
        createdLights = new int[createdLightsList.size()];
        removedLights = new int[removedLightsList.size()];
        modifiedLights = new long[modifiedLightsList.size()];
        
        for (int i = 0; i < createdLights.length; i++)
            createdLights[i] = createdLightsList.get(i);
        for (int i = 0; i < removedLights.length; i++)
            removedLights[i] = removedLightsList.get(i);
        for (int i = 0; i < modifiedLights.length; i++)
            modifiedLights[i] = modifiedLightsList.get(i);

        createdParticles = new PhysicsParticleDefinition[current.particleData.size()];
        
        for (int i = 0; i < createdParticles.length; i++)
            createdParticles[i] = current.particleData.get(i);
        
        lightAmbientColor = current.lightAmbientColor;
    }
    
    @Override
    public void read(Client client, DataInputStream in) throws IOException
    {
        float cameraPositionX = in.readFloat();
        float cameraPositionY = in.readFloat();
        float cameraRotation = in.readFloat();
        float cameraScaleX = in.readFloat();
        float cameraScaleY = in.readFloat();
        
        client.worldRenderer.camera.translation.set(cameraPositionX, cameraPositionY);
        client.worldRenderer.camera.rotation = cameraRotation;
        client.worldRenderer.camera.scale.set(cameraScaleX, cameraScaleY);

        float ambientR = in.readFloat();
        float ambientG = in.readFloat();
        float ambientB = in.readFloat();
        float ambientA = in.readFloat();
        lightAmbientColor = new Color(ambientR, ambientG, ambientB, ambientA);

        int createdComponentsAmount = in.readInt();
        int removedComponentsAmount = in.readInt();
        int modifiedComponentsAmount = in.readInt();

        int createdLightsAmount = in.readInt();
        int removedLightsAmount = in.readInt();
        int modifiedLightsAmount = in.readInt();
        
        int createdParticlesAmount = in.readInt();
        
        createdComponents = new int[createdComponentsAmount];
        removedComponents = new int[removedComponentsAmount];
        modifiedComponents = new long[modifiedComponentsAmount];
        productComponents = new PhysicsComponentModifications[modifiedComponentsAmount];
        
        createdLights = new int[createdLightsAmount];
        removedLights = new int[removedLightsAmount];
        modifiedLights = new long[modifiedLightsAmount];
        productLights = new LightModifications[modifiedLightsAmount];
        
        createdParticles = new PhysicsParticleDefinition[createdParticlesAmount];

        for(int i = 0; i < createdComponentsAmount; i++)
            createdComponents[i] = in.readInt();
        for(int i = 0; i < removedComponentsAmount; i++)
            removedComponents[i] = in.readInt();
        for(int i = 0; i < modifiedComponentsAmount; i++)
        {
            long data = in.readLong();
            modifiedComponents[i] = data;
            
            PhysicsComponentModifications modifications = new PhysicsComponentModifications();
            productComponents[i] = modifications;
            
            if ((data & WorldSnapshotSegment.MODIFIED_POSITION) != 0)
            {
                float positionX = in.readFloat();
                float positionY = in.readFloat();
                
                modifications.positionModified = true;
                modifications.data.position = new Vector2(positionX, positionY);
            }
            
            if ((data & WorldSnapshotSegment.MODIFIED_ROTATION) != 0)
            {
                float rotation = in.readFloat();
                
                modifications.rotationModified = true;
                modifications.data.angle = rotation;
            }
            
            if ((data & WorldSnapshotSegment.MODIFIED_SHAPE) != 0)
            {
                int typeOrdinal = in.readInt();
                PhysicsComponentShapeType type = PhysicsComponentShapeType.values()[typeOrdinal];

                modifications.shapeModified = true;
                modifications.data.shapeType = type;
                
                switch (type)
                {
                case CIRCLE:
                    float radius = in.readFloat();
                    modifications.data.radius = radius;
                    break;
                case POLYGON:
                    int polygonN = in.readInt();
                    modifications.data.vertices = new Vector2[polygonN];
                    for (int j = 0; j < polygonN; j++)
                    {
                        float vertexX = in.readFloat();
                        float vertexY = in.readFloat();
                        modifications.data.vertices[j] = new Vector2(vertexX, vertexY);
                    }
                    break;
                case TRIANGLE_GROUP:
                    int triangleN = in.readInt();
                    modifications.data.vertices = new Vector2[triangleN * 3];
                    for (int j = 0; j < triangleN * 3; j++)
                    {
                        float vertexX = in.readFloat();
                        float vertexY = in.readFloat();
                        modifications.data.vertices[j] = new Vector2(vertexX, vertexY);
                    }
                    break;
                }
            }
            
            if ((data & WorldSnapshotSegment.MODIFIED_PHYSICS_DATA) != 0)
            {
                int typeOrdinal = in.readInt();
                
                modifications.physicsDataModified = true;
                modifications.data.type = PhysicsComponentType.values()[typeOrdinal];
            }
        }

        for(int i = 0; i < createdLightsAmount; i++)
            createdLights[i] = in.readInt();
        for(int i = 0; i < removedLightsAmount; i++)
            removedLights[i] = in.readInt();
        for(int i = 0; i < modifiedLightsAmount; i++)
        {
            long data = in.readLong();
            modifiedLights[i] = data;
            
            LightModifications modifications = new LightModifications();
            productLights[i] = modifications;
            
            if ((data & WorldSnapshotSegment.MODIFIED_POSITION) != 0)
            {
                float positionX = in.readFloat();
                float positionY = in.readFloat();
                
                modifications.positionModified = true;
                modifications.data.position = new Vector2(positionX, positionY);
            }
            
            if ((data & WorldSnapshotSegment.MODIFIED_SHAPE) != 0)
            {
                float radius = in.readFloat();
                float colorR = in.readFloat();
                float colorG = in.readFloat();
                float colorB = in.readFloat();
                float colorA = in.readFloat();

                modifications.shapeModified = true;
                modifications.data.radius = radius;
                modifications.data.color = new Color(colorR, colorG, colorB, colorA);
            }
        }
        
        for(int i = 0; i < createdParticlesAmount; i++)
        {
            float positionX = in.readFloat();
            float positionY = in.readFloat();
            float angle = in.readFloat();
            float angularVelocity = in.readFloat();
            float linearVelocityX = in.readFloat();
            float linearVelocityY = in.readFloat();

            float size = in.readFloat();
            float density = in.readFloat();
            float restitution = in.readFloat();

            float angularDamping = in.readFloat();
            float linearDamping = in.readFloat();

            float lifetime = in.readFloat();
            boolean destroyOnCollision = in.readBoolean();
            
            PhysicsParticleDefinition particleDef = new PhysicsParticleDefinition();
            particleDef.position.set(positionX, positionY);
            particleDef.angle = angle;
            particleDef.angularVelocity = angularVelocity;
            particleDef.linearVelocity.set(linearVelocityX, linearVelocityY);

            particleDef.size = size;
            particleDef.density = density;
            particleDef.restitution = restitution;
            
            particleDef.angularDamping = angularDamping;
            particleDef.linearDamping = linearDamping;
            
            particleDef.lifetime = lifetime;
            particleDef.destroyOnCollision = destroyOnCollision;
            
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
            
            createdParticles[i] = particleDef;
        }
    }
    
    @Override
    public void write(Server server, ServerUser user) throws IOException
    {
        user.outputStream.writeFloat(user.camera.translation.x);
        user.outputStream.writeFloat(user.camera.translation.y);
        user.outputStream.writeFloat(user.camera.rotation);
        user.outputStream.writeFloat(user.camera.scale.x);
        user.outputStream.writeFloat(user.camera.scale.y);

        user.outputStream.writeFloat(lightAmbientColor.r);
        user.outputStream.writeFloat(lightAmbientColor.g);
        user.outputStream.writeFloat(lightAmbientColor.b);
        user.outputStream.writeFloat(lightAmbientColor.a);
        
        user.outputStream.writeInt(createdComponents.length);
        user.outputStream.writeInt(removedComponents.length);
        user.outputStream.writeInt(modifiedComponents.length);
        
        user.outputStream.writeInt(createdLights.length);
        user.outputStream.writeInt(removedLights.length);
        user.outputStream.writeInt(modifiedLights.length);
        
        user.outputStream.writeInt(createdParticles.length);

        for (int key : createdComponents) user.outputStream.writeInt(key);
        for (int key : removedComponents) user.outputStream.writeInt(key);
        for (long data : modifiedComponents)
        {
            user.outputStream.writeLong(data);
            
            PhysicsComponentSnapshot compo = full.componentData.get((int) (data & 0xFFFFFFFF));
            
            if ((data & WorldSnapshotSegment.MODIFIED_POSITION) != 0)
            {
                user.outputStream.writeFloat(compo.position.x);
                user.outputStream.writeFloat(compo.position.y);
            }
            
            if ((data & WorldSnapshotSegment.MODIFIED_ROTATION) != 0)
                user.outputStream.writeFloat(compo.angle);
            
            if ((data & WorldSnapshotSegment.MODIFIED_SHAPE) != 0)
            {
                user.outputStream.writeInt(compo.shapeType.ordinal());
                switch (compo.shapeType)
                {
                case CIRCLE:
                    user.outputStream.writeFloat(compo.radius);
                    break;
                case POLYGON:
                    user.outputStream.writeInt(compo.vertices.length);
                    for (int i = 0; i < compo.vertices.length; i++)
                    {
                        user.outputStream.writeFloat(compo.vertices[i].x);
                        user.outputStream.writeFloat(compo.vertices[i].y);
                    }
                    break;
                case TRIANGLE_GROUP:
                    user.outputStream.writeInt(compo.vertices.length / 3);
                    for (int i = 0; i < compo.vertices.length; i++)
                    {
                        user.outputStream.writeFloat(compo.vertices[i].x);
                        user.outputStream.writeFloat(compo.vertices[i].y);
                    }
                    break;
                }
            }
            
            if ((data & WorldSnapshotSegment.MODIFIED_PHYSICS_DATA) != 0)
                user.outputStream.writeInt(compo.type.ordinal());
        }

        for (int key : createdLights) user.outputStream.writeInt(key);
        for (int key : removedLights) user.outputStream.writeInt(key);
        for (long data : modifiedLights)
        {
            user.outputStream.writeLong(data);
            
            LightData light = full.lightData.get((int) (data & 0xFFFFFFFF));
            
            if ((data & WorldSnapshotSegment.MODIFIED_POSITION) != 0)
            {
                user.outputStream.writeFloat(light.position.x);
                user.outputStream.writeFloat(light.position.y);
            }
            
            if ((data & WorldSnapshotSegment.MODIFIED_SHAPE) != 0)
            {
                user.outputStream.writeFloat(light.radius);
                
                user.outputStream.writeFloat(light.color.r);
                user.outputStream.writeFloat(light.color.g);
                user.outputStream.writeFloat(light.color.b);
                user.outputStream.writeFloat(light.color.a);
            }
        }
        
        for (PhysicsParticleDefinition particleDef : createdParticles)
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
