package net.lodoma.lime.mod;

import java.util.HashMap;
import java.util.Map;

public class DataBundle
{
    private Map<String, Object> data;
    
    public DataBundle(String[] names, Object[] values)
    {
        if(names.length != values.length)
            throw new IllegalArgumentException();
        
        data = new HashMap<String, Object>();
        for(int i = 0; i < names.length; i++)
            data.put(names[i], values[i]);
    }
    
    public DataBundle(Map<String, Object> data)
    {
        this.data = new HashMap<String, Object>(data);
    }
    
    public Object get(String name)
    {
        return data.get(name);
    }
}
