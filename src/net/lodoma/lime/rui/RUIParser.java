package net.lodoma.lime.rui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.Lime;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.OsHelper;
import net.lodoma.lime.util.Pair;

public class RUIParser
{
    private List<List<String>> tokens;
    private int cline;
    private int ctoken;
    
    public RUIParser(String filepath)
    {
        tokens = new ArrayList<List<String>>();
        
        File file = new File(OsHelper.JARPATH + "res/rui/" + filepath + ".rui");
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                line = line.trim();
                if (line.equals("")) continue;
                line += " ";
                
                List<String> ctokens = new ArrayList<String>();
                
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < line.length(); i++)
                {
                    char c = line.charAt(i);
                    if (Character.isWhitespace(c))
                    {
                        if (builder.length() > 0)
                        {
                            ctokens.add(builder.toString());
                            builder.setLength(0);
                        }
                    }
                    else
                    {
                        if (c == '"')
                        {
                            while (true)
                            {
                                i++;
                                if (i == line.length())
                                    throw new IOException("String not closed");
                                c = line.charAt(i);
                                
                                if (c == '"')
                                    break;
                                builder.append(c);
                            }
                        }
                        else
                        {
                            builder.append(c);
                        }
                    }
                }
                
                tokens.add(ctokens);
            }
        }
        catch (IOException e)
        {
            Lime.LOGGER.C("Exception while parsing RUI file");
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
    }
    
    public boolean nextLine()
    {
        if (ctoken < tokens.get(cline).size())
        {
            Lime.LOGGER.C("Error while parsing RUI file - excess data");
            Lime.forceExit(null);
        }
        cline++;
        ctoken = 0;
        return cline >= tokens.size();
    }
    
    public boolean lastLine()
    {
        return cline >= tokens.size();
    }
    
    public String nextToken()
    {
        if (cline >= tokens.size() || ctoken >= tokens.get(cline).size())
        {
            Lime.LOGGER.C("Error while parsing RUI file - lack of data");
            Lime.forceExit(null);
        }
        ctoken++;
        return tokens.get(cline).get(ctoken - 1);
    }
    
    public String peekToken()
    {
        if (cline >= tokens.size() || ctoken >= tokens.get(cline).size())
        {
            Lime.LOGGER.C("Error while parsing RUI file - lack of data");
            Lime.forceExit(null);
        }
        return tokens.get(cline).get(ctoken);
    }

    private static class ParseResults
    {
        Map<String, RUIParserData> valuemapMap = new HashMap<String, RUIParserData>();
        Map<String, Map<String, RUIParserData>> elementMap = new HashMap<String, Map<String, RUIParserData>>();
        Map<String, RUIGroup> groupMap = new HashMap<String, RUIGroup>();
    }
    
    private RUIElement createElement(RUIParserData data, RUIElement parent, ParseResults results)
    {
        RUIElement element = null;
        switch (data.type)
        {
        case "container":
            element = new RUIElement(parent);
            break;
        case "label":
            element = new RUILabel(parent);
            break;
        case "button":
            element = new RUIButton(parent);
            break;
        case "toggle":
            element = new RUIToggle(parent);
            break;
        case "unordered-list":
            element = new RUIUnorderedList(parent);
            break;
        case "progress-bar":
            element = new RUIProgressBar(parent);
            break;
        case "slider":
            element = new RUISlider(parent);
            break;
        case "text-field":
            element = new RUITextField(parent);
            break;
        case "choice":
            element = new RUIChoice(parent);
            break;
        default:
            Lime.LOGGER.C("Error while parsing RUI file - invalid element type");
            Lime.forceExit(null);
        }
        
        String group = data.get("group");
        if (group != null)
            element.group = results.groupMap.get(group);
        
        element.loadData(data);
        
        return element;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void merge(Map src, Map dest)
    {
        Set keys = src.keySet();
        for (Object key : keys)
        {
            Object value = src.get(key);
            if ((value instanceof Map) && dest.containsKey(key))
                merge((Map) value, (Map) dest.get(key));
            else
                dest.put(key, value);
        }
    }
    
    private ParseResults parse()
    {
        ParseResults results = new ParseResults();
        
        while (!lastLine())
        {
            if (peekToken().equals("INCLUDE"))
            {
                nextToken();
                String path = nextToken();
                nextLine();
                
                RUIParser includeParser = new RUIParser(path);
                ParseResults parsed = includeParser.parse();
                merge(parsed.valuemapMap, results.valuemapMap);
                merge(parsed.elementMap, results.elementMap);
                merge(parsed.groupMap, results.groupMap);
                
                continue;
            }
            else if (peekToken().equals("GROUP"))
            {
                nextToken();
                results.groupMap.put(nextToken(), new RUIGroup());
                nextLine();
                
                continue;
            }
            
            RUIParserData data = new RUIParserData();
            data.parse(this);
            if (data.isValuemap)
            {
                results.valuemapMap.put(data.name, data);
            }
            else
            {
                if (!results.elementMap.containsKey(data.parent))
                    results.elementMap.put(data.parent, new HashMap<String, RUIParserData>());
                results.elementMap.get(data.parent).put(data.name, data);
            }
        }
        
        return results;
    }
    
    public void load(RUIElement root)
    {
        // Parse elements and valuemaps
        ParseResults parsed = parse();
        
        // Resolve valuemap requests
        Collection<Map<String, RUIParserData>> elementMaps = parsed.elementMap.values();
        for (Map<String, RUIParserData> elementsMap : elementMaps)
        {
            Collection<RUIParserData> elements = elementsMap.values();
            for (RUIParserData element : elements)
                for (String valuemapName : element.valuemapList)
                {
                    RUIParserData valuemap = parsed.valuemapMap.get(valuemapName);
                    valuemap.merge(element);
                }
        }
        
        // Create elements
        Set<Pair<String, RUIElement>> parentList = new HashSet<Pair<String, RUIElement>>();
        parentList.add(new Pair<String, RUIElement>("ROOT", root));
        
        while (!parentList.isEmpty())
        {
            Set<Pair<String, RUIElement>> parentListCopy = new HashSet<Pair<String, RUIElement>>(parentList);
            for (Pair<String, RUIElement> parent : parentListCopy)
            {
                parentList.remove(parent);
                if (!parsed.elementMap.containsKey(parent.first)) continue;
                
                Collection<RUIParserData> elements = parsed.elementMap.get(parent.first).values();
                for (RUIParserData element : elements)
                {
                    RUIElement newElement = createElement(element, parent.second, parsed); 
                    parent.second.addChild(element.name, newElement);
                    parentList.add(new Pair<String, RUIElement>(element.name, newElement));
                }
            }
        }
    }
    
    public static RUIValue parseSize(String size)
    {
        try
        {
            if (size.endsWith("%"))
                return new RUIValue(Float.parseFloat(size.substring(0, size.length() - 1)) / 100.0f);
            else if (size.endsWith("px"))
                return new RUIValue(-Float.parseFloat(size.substring(0, size.length() - 2)));
            else throw new NumberFormatException();
        }
        catch (NumberFormatException e)
        {
            Lime.LOGGER.C("Error while parsing RUI file - invalid number format");
            Lime.forceExit(null);
            return new RUIValue(0.0f);
        }
    }
    
    public static RUIValue parseColor(String color)
    {
        return new RUIValue(new Color(
            Integer.parseInt(color.substring(0, 2), 16) / 255.0f,
            Integer.parseInt(color.substring(2, 4), 16) / 255.0f,
            Integer.parseInt(color.substring(4, 6), 16) / 255.0f,
            Integer.parseInt(color.substring(6, 8), 16) / 255.0f
        ));
    }
    
    public static RUIValue parseBool(String bool)
    {
        if (bool.equals("true")) return RUIValue.BOOLEAN_TRUE;
        else if (bool.equals("false")) return RUIValue.BOOLEAN_FALSE;

        Lime.LOGGER.C("Error while parsing RUI file - invalid boolean");
        Lime.forceExit(null);
        return RUIValue.BOOLEAN_FALSE;
    }
    
    public static RUIValue parseInteger(String integer)
    {
        int intValue = 0;
        try
        {
            intValue = Integer.parseInt(integer);
        }
        catch (NumberFormatException e)
        {
            Lime.LOGGER.C("Error while parsing RUI file - invalid number format");
            Lime.forceExit(null);
        }
        return new RUIValue(intValue);
    }
}
