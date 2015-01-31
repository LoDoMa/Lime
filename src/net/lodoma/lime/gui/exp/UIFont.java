package net.lodoma.lime.gui.exp;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.util.TrueTypeFont;

public class UIFont
{
    private static Map<String, Map<Integer, Map<Integer, TrueTypeFont>>> ttfs = new HashMap<String, Map<Integer, Map<Integer, TrueTypeFont>>>();

    public TrueTypeFont ttf;
    public int alignment;
    
    public UIFont(String fontName, int size, int style, int alignment)
    {
        if (!ttfs.containsKey(fontName))
            ttfs.put(fontName, new HashMap<Integer, Map<Integer, TrueTypeFont>>());
        Map<Integer, Map<Integer, TrueTypeFont>> ttfs2 = ttfs.get(fontName);
        
        if (!ttfs2.containsKey(size))
            ttfs2.put(size, new HashMap<Integer, TrueTypeFont>());
        Map<Integer, TrueTypeFont> ttfs3 = ttfs2.get(size);
        
        if (!ttfs3.containsKey(style))
        {
            char[] additionalCharacters = new char[Character.MAX_VALUE - 256 + 1];
            for (int i = 256; i <= Character.MAX_VALUE; i++)
                additionalCharacters[i - 256] = (char) i;
            ttf = new TrueTypeFont(new Font(fontName, style, size), true, additionalCharacters);
            
            ttfs3.put(style, ttf);
        }
        else
        {
            ttf = ttfs3.get(style);
        }
        
        this.alignment = alignment;
    }
}
