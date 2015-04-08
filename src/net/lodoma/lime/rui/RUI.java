package net.lodoma.lime.rui;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.util.TrueTypeFont;

public class RUI
{
    private static final int FONT_SIZE = 32;
    private static final Map<String, TrueTypeFont> fonts = new HashMap<String, TrueTypeFont>();
    
    public static TrueTypeFont getFont(String fontname)
    {
        synchronized (fonts)
        {
            TrueTypeFont font = fonts.get(fontname);
            if (font == null)
            {
                char[] chars = new char[Character.MAX_VALUE - 255 + 1];
                for (int c = 256; c <= Character.MAX_VALUE; c++)
                    chars[c - 256] = (char) c;
                font = new TrueTypeFont(new Font(fontname, Font.PLAIN, FONT_SIZE), true, chars);
                fonts.put(fontname, font);
            }
            return font;
        }
    }
}
