package net.lodoma.lime.world;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.shader.light.LightModifications;
import net.lodoma.lime.snapshot.SnapshotData;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.physics.PhysicsComponentModifications;
import net.lodoma.lime.world.physics.PhysicsComponentShapeType;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class WorldSnapshotSegment implements SnapshotData
{
    public static final long MODIFIED_ALL              = 0xFFFFFFFFl << 32;
    public static final long MODIFIED_POSITION         = 0x80000000l << 32;
    public static final long MODIFIED_ROTATION         = 0x40000000l << 32;
    public static final long MODIFIED_SHAPE            = 0x20000000l << 32;
    
    public int[] createdComponents;
    public int[] removedComponents;
    public long[] modifiedComponents;
    public PhysicsComponentModifications[] productComponents;
    
    public int[] createdLights;
    public int[] removedLights;
    public long[] modifiedLights;
    public LightModifications[] productLights;
    
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
                
                if (!currentCompo.type.equals(previousCompo.type))
                    modified |= MODIFIED_SHAPE;
                else switch (currentCompo.type)
                {
                case CIRCLE:
                    if (currentCompo.radius != previousCompo.radius)
                        modified |= MODIFIED_SHAPE;
                    break;
                case POLYGON:
                    if (!Arrays.equals(currentCompo.vertices, previousCompo.vertices))
                        modified |= MODIFIED_SHAPE;
                    break;
                }
                
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

        int createdComponentsAmount = in.readInt();
        int removedComponentsAmount = in.readInt();
        int modifiedComponentsAmount = in.readInt();

        int createdLightsAmount = in.readInt();
        int removedLightsAmount = in.readInt();
        int modifiedLightsAmount = in.readInt();
        
        createdComponents = new int[createdComponentsAmount];
        removedComponents = new int[removedComponentsAmount];
        modifiedComponents = new long[modifiedComponentsAmount];
        productComponents = new PhysicsComponentModifications[modifiedComponentsAmount];
        
        createdLights = new int[createdLightsAmount];
        removedLights = new int[removedLightsAmount];
        modifiedLights = new long[modifiedLightsAmount];
        productLights = new LightModifications[modifiedLightsAmount];

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
                modifications.data.type = type;
                
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
                }
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
    }
    
    @Override
    public void write(Server server, ServerUser user) throws IOException
    {
        user.outputStream.writeFloat(user.camera.translation.x);
        user.outputStream.writeFloat(user.camera.translation.y);
        user.outputStream.writeFloat(user.camera.rotation);
        user.outputStream.writeFloat(user.camera.scale.x);
        user.outputStream.writeFloat(user.camera.scale.y);
        
        user.outputStream.writeInt(createdComponents.length);
        user.outputStream.writeInt(removedComponents.length);
        user.outputStream.writeInt(modifiedComponents.length);
        
        user.outputStream.writeInt(createdLights.length);
        user.outputStream.writeInt(removedLights.length);
        user.outputStream.writeInt(modifiedLights.length);

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
                user.outputStream.writeInt(compo.type.ordinal());
                switch (compo.type)
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
                }
            }
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
    }
}