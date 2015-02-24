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
            if (objects.containsKey(identifier))
                throw new IdentityException("duplicate identifier in managed IdentityPool");
        }
        objects.put(identifier, object);
        return identifier;
    }
    
    // NOTE: This isn't really a good solution
    public synchronized int addManaged(T object)
    {
        int identifier = object.getIdentifier();
        if (objects.containsKey(identifier))
            throw new IdentityException("duplicate identifier in IdentityPool [managed addition]");
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
    
    public boolean isEmpty()
    {
        return objects.isEmpty();
    }
    
    public void clear()
    {
        objects.clear();
    }

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
        Set<T> set = new HashSet<T>(objects.values());
        for (T value : set)
            consumer.accept(value);
    }
    
    private int nextID()
    {
        if (objects.size() == Integer.MAX_VALUE)
             throw new PoolOverflowError("IdentityPool overflow; too many objects");
        while (objects.containsKey(idCounter++));
        return idCounter - 1;
    }
}
