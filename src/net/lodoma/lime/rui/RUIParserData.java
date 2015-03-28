package net.lodoma.lime.rui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RUIParserData
{
    public boolean isValuemap;
    public String name;
    public String parent;
    public String type;
    
    public List<String> valuemapList = new ArrayList<String>();
    public Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();
    
    public void copy(String category, RUIValueType type, RUIValueMap map)
    {
        Set<String> stateNames = values.keySet();
        for (String stateName : stateNames)
        {
            Map<String, String> stateMap = values.get(stateName);
            if (stateMap != null && stateMap.containsKey(category))
                map.set(stateName, category, toRUIValue(stateMap.get(category), type));
        }
    }
    
    public void copyValuemap(RUIParserData element)
    {
        Set<String> states = values.keySet();
        for (String state : states)
        {
            Map<String, String> map = values.get(state);
            Set<String> categories = map.keySet();
            for (String category : categories)
            {
                String value = map.get(category);
                
                if (!element.values.containsKey(state))
                    element.values.put(state, new HashMap<String, String>());
                element.values.get(state).put(category, value);
            }
        }
    }
    
    private RUIValue toRUIValue(String value, RUIValueType type)
    {
        switch (type)
        {
        case BOOLEAN: return RUIParser.parseBool(value);
        case INTEGER: return RUIParser.parseInteger(value);
        case COLOR: return RUIParser.parseColor(value);
        case SIZE: return RUIParser.parseSize(value);
        case STRING: return new RUIValue(value);
        default: throw new IllegalStateException();
        }
    }
    
    public void parse(RUIParser parser)
    {
        name = parser.nextToken();
        if (name.startsWith("@"))
        {
            isValuemap = true;
        }
        else
        {
            isValuemap = false;
            parent = parser.nextToken();
            type = parser.nextToken();
        }
        
        parser.nextLine();
        
        while (!parser.lastLine())
        {
            String marker = parser.peekToken();
            if (!marker.equals("-")) break;
            parser.nextToken();
            
            String state = "default";
            if (parser.peekToken().startsWith("@"))
            {
                if (isValuemap)
                    throw new UnsupportedOperationException();
                valuemapList.add(parser.nextToken());
            }
            else
            {
                if (parser.peekToken().equals("STATE"))
                {
                    parser.nextToken();
                    state = parser.nextToken();
                }
                
                String category = parser.nextToken();
                String value = parser.nextToken();
                
                if (!values.containsKey(state))
                    values.put(state, new HashMap<String, String>());
                values.get(state).put(category, value);
            }
            
            parser.nextLine();
        }
    }
}
