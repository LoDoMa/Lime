package net.lodoma.lime.gui;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.util.TrueTypeFont;

public class UIFont
{
    // This might change, but currently, there is no need for bigger font size.
    public static final int DEFAULT_FONT_SIZE = 42;
    
    private static Map<String, TrueTypeFont> ttfs = new HashMap<String, TrueTypeFont>();

    public TrueTypeFont ttf;
    public int horizontalAlignment;
    public int verticalAlignment;
    
    public UIFont(String fontName, int horizontalAlignment, int verticalAlignment)
    {
        if (!ttfs.containsKey(fontName))
        {
            ttf = new TrueTypeFont(new Font(fontName, Font.PLAIN, DEFAULT_FONT_SIZE), true, null);
            ttfs.put(fontName, ttf);
        }
        else
        {
            ttf = ttfs.get(fontName);
        }

        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }
}
