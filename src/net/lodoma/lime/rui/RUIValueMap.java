package net.lodoma.lime.rui;

import java.util.HashMap;
import java.util.Map;

public class RUIValueMap
{
    private Map<String, Map<String, RUIValue>> values;
    
    public RUIValueMap()
    {
        values = new HashMap<String, Map<String, RUIValue>>();
    }
    
    public RUIValue get(String state, String category)
    {
        if (!values.containsKey(state))
        {
            if (state != "default")
                return get("default", category);
            throw new IllegalStateException();
        }
        if (!values.get(state).containsKey(category))
        {
            if (state != "default")
                return get("default", category);
            throw new IllegalStateException();
        }
        return values.get(state).get(category);
    }
    
    public void set(String state, String category, RUIValue value)
    {
        if (!values.containsKey(state))
            values.put(state, new HashMap<String, RUIValue>());
        values.get(state).put(category, value);
    }
}
