package net.lodoma.lime.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class IdentityPool<T extends Identifiable<Integer>>
{
    private Map<Integer, T> objects;
    private int idCounter;
    private boolean managed;
    
    public IdentityPool()
    {
        this(false);
    }
    
    public IdentityPool(boolean managed)
    {
        objects = new HashMap<Integer, T>();
        idCounter = 0;
        this.managed = managed;
    }
    
    public synchronized int add(T object)
    {
        int identifier;
        if (!managed)
        {
            identifier = nextID();
            object.setIdentifier(identifier);
        }
        else
        {
            identifier = object.getIdentifier();
        }
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
        if (objects.size() == Integer.MAX_VALUE)
             throw new PoolOverflowError("IdentityPool overflow; too many objects");
        while (objects.containsKey(idCounter++));
        return idCounter - 1;
    }
}
