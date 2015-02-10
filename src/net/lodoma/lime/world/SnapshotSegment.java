package net.lodoma.lime.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.shader.light.LightModifications;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.physics.PhysicsComponentModifications;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class SnapshotSegment
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
    
    public Snapshot full;
    
    public SnapshotSegment()
    {
        
    }
    
    public SnapshotSegment(Snapshot current, Snapshot previous)
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
}
