package net.lodoma.lime.rui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RUIParserDefinition
{
    public String parent;
    public String type;
    
    public Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();
    
    private RUIValue valueToRUI(String value, RUIValueType type)
    {
        switch (type)
        {
        case BOOLEAN: return RUIParser.parseBool(value);
        case INTEGER: return RUIParser.parseAlignment(value);
        case COLOR: return RUIParser.parseColor(value);
        case SIZE: return RUIParser.parseSize(value);
        case STRING: return new RUIValue(value);
        default: throw new IllegalStateException();
        }
    }
    
    public void store(String category, RUIValueType type, RUIValueMap map)
    {
        Set<String> stateNames = values.keySet();
        for (String stateName : stateNames)
        {
            Map<String, String> stateMap = values.get(stateName);
            if (stateMap != null && stateMap.containsKey(category))
                map.set(stateName, category, valueToRUI(stateMap.get(category), type));
        }
    }
    
    public String parse(RUIParser parser)
    {
        String name = parser.nextToken();
        parent = parser.nextToken();
        type = parser.nextToken();
        
        parser.nextLine();
        
        while (!parser.lastLine())
        {
            String marker = parser.peekToken();
            if (!marker.equals("-")) break;
            parser.nextToken();
            
            String state = "default";
            if (parser.peekToken().equals("STATE"))
            {
                parser.nextToken();
                state = parser.nextToken();
            }
            
            String category = parser.nextToken();
            String value = parser.nextToken();
            parser.nextLine();
            
            if (!values.containsKey(state))
                values.put(state, new HashMap<String, String>());
            values.get(state).put(category, value);
        }
        
        return name;
    }
}
