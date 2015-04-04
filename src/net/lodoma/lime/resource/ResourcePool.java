package net.lodoma.lime.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.Lime;

public class ResourcePool
{
    private static Map<ResourceType, Map<String, Resource>> resources = new HashMap<ResourceType, Map<String, Resource>>();
    static
    {
        ResourceType[] types = ResourceType.values();
        for (ResourceType type : types)
            resources.put(type, new HashMap<String, Resource>());
    }
    
    private static List<Resource> createList = new ArrayList<Resource>();
    private static List<Resource> destroyList = new ArrayList<Resource>();

    public static Object get(String name, ResourceType type)
    {
        synchronized (resources)
        {
            return resources.get(type).get(name).get();
        }
    }
    
    public static void update(double timeDelta)
    {
        synchronized (resources)
        {
            resources.forEach((ResourceType type, Map<String, Resource> map) -> {
                map.forEach((String name, Resource resource) -> {
                    resource.update(timeDelta);
                });
            });
        }
    }
    
    public static void referenceUp(String name, ResourceType type)
    {
        synchronized (resources)
        {
            Resource resource = resources.get(type).get(name);
            if (resource == null)
            {
                resource = type.create.get();
                resource.name = name;
                resource.type = type;
                resource.referenceCount = 0;
                resources.get(type).put(name, resource);
                createList.add(resource);
                Lime.LOGGER.I("Added resource " + name + " to the creation list");
            }
            
            resource.referenceCount++;
        }
    }
    
    public static void referenceDown(String name, ResourceType type)
    {
        synchronized (resources)
        {
            Resource resource = resources.get(type).get(name);
            if (resource == null)
            {
                Lime.LOGGER.C("Attempt to \"reference down\" a resource that doesn't exist.");
                Lime.forceExit(null);
            }
            
            resource.referenceCount--;
            if (resource.referenceCount <= 0)
            {
                resources.get(type).remove(name);
                
                destroyList.add(resource);
                Lime.LOGGER.I("Added resource " + name + " to the destruction list");
            }
        }
    }
    
    public static void create()
    {
        synchronized (resources)
        {
            while (createList.size() > 0)
            {
                List<Resource> ncl = new ArrayList<Resource>(createList);
                createList.clear();
                
                for (Resource resource : ncl)
                    resource.create();
            }
        }
    }
    
    public static void destroy()
    {
        synchronized (resources)
        {
            while (destroyList.size() > 0)
            {
                List<Resource> ndl = new ArrayList<Resource>(destroyList);
                destroyList.clear();
                
                for (Resource resource : ndl)
                    resource.destroy();
            }
        }
    }
    
    public static void checkClean()
    {
        synchronized (resources)
        {
            resources.forEach((ResourceType type, Map<String, Resource> map) -> {
                map.forEach((String name, Resource resource) -> {
                    resource.destroy();
                    Lime.LOGGER.W("Deleted resource " + name + " during a check clean.");
                });
                map.clear();
            });
        }
    }
}
