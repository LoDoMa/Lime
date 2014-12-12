package net.lodoma.lime.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/* NOTE: Maybe GeneratedIdentityPool functionality should somehow be implemented by IdentityPool?
         Otherwise, these classes should be renamed; IdentityPool->ManagedIdentityPool, GeneratedIdentityPool->IdentityPool */

public class GeneratedIdentityPool<T extends Identifiable<Integer>>
{
    private Map<Integer, T> objects;
    private int idCounter;
    
    public GeneratedIdentityPool()
    {
        objects = new HashMap<Integer, T>();
        idCounter = 0;
    }
    
    public synchronized int add(T object)
    {
        int identifier = nextID();
        object.setIdentifier(identifier);
        objects.put(identifier, object);
        return identifier;
    }
    
    public synchronized boolean has(int identifier)
    {
        return objects.containsKey(identifier);
    }
    
    public synchronized T get(int identifier)
    {
        return objects.get(identifier);
    }
    
    public synchronized void remove(int identifier)
    {
        objects.remove(identifier);
    }
    
    public void remove(T identifiable)
    {
        remove(identifiable.getIdentifier());
    }
    
    /* NOTE: Is getIdentifierSet method needed?
             Only elements whose identifiers are known should ever be accessed. */
    
    @Deprecated
    public synchronized Set<Integer> getIdentifierSet()
    {
        return new HashSet<Integer>(objects.keySet());
    }

    public synchronized List<T> getObjectList()
    {
        return new ArrayList<T>(objects.values());
    }

    public synchronized int size()
    {
        return objects.size();
    }
    
    public void foreach(Consumer<T> consumer)
    {
        List<T> objects = getObjectList();
        for (T object : objects)
            consumer.accept(object);
    }
    
    private int nextID()
    {
        // NOTE: This while loop may not ever stop, but that's unlikely 
        while (objects.containsKey(idCounter++));
        return idCounter - 1;
    }
}
