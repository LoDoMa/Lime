package net.lodoma.lime.rui;

import java.awt.Font;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.lodoma.lime.localization.Language;
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
                char[] additionalChars = Language.getCharset();
                System.out.println(Arrays.toString(additionalChars));
                font = new TrueTypeFont(new Font(fontname, Font.PLAIN, FONT_SIZE), true, additionalChars);
                fonts.put(fontname, font);
            }
            return font;
        }
    }
    
    public static void reload()
    {
        synchronized (fonts)
        {
            Collection<TrueTypeFont> fontList = fonts.values();
            for (TrueTypeFont font : fontList)
                font.destroy();
            fonts.clear();
        }
    }
}
