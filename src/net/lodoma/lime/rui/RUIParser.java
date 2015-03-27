package net.lodoma.lime.rui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.Lime;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.OsHelper;
import net.lodoma.lime.util.Pair;
import net.lodoma.lime.util.TrueTypeFont;

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
    
    private RUIElement createElement(RUIParserDefinition definition, RUIElement parent)
    {
        RUIElement element = null;
        switch (definition.type)
        {
        case "container":
        	element = new RUIElement(parent);
        	break;
        case "button":
            element = new RUIButton(parent);
            break;
        case "label":
            element = new RUILabel(parent);
            break;
        default:
            Lime.LOGGER.C("Error while parsing RUI file - invalid element type");
            Lime.forceExit(null);
        }
        element.loadDefinition(definition);
        
        return element;
    }
    
    public void load(RUIElement root)
    {
        Map<String, RUIParserDefinition> definitions = new HashMap<String, RUIParserDefinition>();
        
        while (!lastLine())
        {
            RUIParserDefinition definition = new RUIParserDefinition();
            String name = definition.parse(this);
            definitions.put(name, definition);
        }

        Set<Pair<String, RUIElement>> parentList = new HashSet<Pair<String, RUIElement>>();
        parentList.add(new Pair<String, RUIElement>("ROOT", root));
        
        while (!definitions.isEmpty())
        {
            Set<Pair<String, RUIElement>> parentListCopy = new HashSet<Pair<String, RUIElement>>(parentList);
            for (Pair<String, RUIElement> parent : parentListCopy)
            {
                Set<String> definitionNames = new HashSet<String>(definitions.keySet());
                for (String definitionName : definitionNames)
                {
                    RUIParserDefinition definition = definitions.get(definitionName);
                    if (definition.parent.equals(parent.first))
                    {
                        RUIElement newElement = createElement(definition, parent.second);
                        parent.second.addChild(definitionName, newElement);
                        parentList.add(new Pair<String, RUIElement>(definitionName, newElement));
                        definitions.remove(definitionName);
                    }
                }
                parentList.remove(parent.first);
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
    
    public static RUIValue parseAlignment(String align)
    {
        if (align.equals("left") || align.equals("top"))
            return new RUIValue(TrueTypeFont.ALIGN_LEFT);
        else if (align.equals("center"))
            return new RUIValue(TrueTypeFont.ALIGN_CENTER);
        else if (align.equals("right") || align.equals("bottom"))
            return new RUIValue(TrueTypeFont.ALIGN_RIGHT);

        Lime.LOGGER.C("Error while parsing RUI file - invalid alignment");
        Lime.forceExit(null);
        return new RUIValue(0);
    }
}
