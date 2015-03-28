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
        String[] states = (state + ":default").split(":");
        
        for (String tryState : states)
        {
            Map<String, RUIValue> valueMap = values.get(tryState);
            if (valueMap == null)
                continue;
            RUIValue value = valueMap.get(category);
            if (value == null)
                continue;
            return value;
        }
        throw new IllegalStateException();
    }
    
    public void set(String state, String category, RUIValue value)
    {
        if (!values.containsKey(state))
            values.put(state, new HashMap<String, RUIValue>());
        values.get(state).put(category, value);
    }
}
