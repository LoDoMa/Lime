package net.lodoma.lime.rui;

import java.util.HashMap;
import java.util.Map;

public class RUIParserDefinition
{
    public String parent;
    public String type;
    
    public Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();
    
    public String get(String state, String category, String defaultValue)
    {
        if (!values.containsKey(state))
        {
            if (state != "default")
                return get("default", category, defaultValue);
            return defaultValue;
        }
        if (!values.get(state).containsKey(category))
        {
            if (state != "default")
                return get("default", category, defaultValue);
            return defaultValue;
        }
        return values.get(state).get(category);
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
