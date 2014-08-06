package net.lodoma.lime.gui.simple;

import java.awt.Font;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.util.TrueTypeFont;

public class SimpleText extends Text
{
    private static final String FONT_NAME = "My type of font";
    private static final Color TEXT_COLOR = new Color(0.0f, 0.5f, 1.0f);
    
    public SimpleText(float x, float y, float width, float height, String text, Color color, int alignment)
    {
        super(x + width / 2.0f, y, (height * 0.60f), (height * 0.75f), text, color, FONT_NAME, Font.PLAIN, alignment);
    }
    
    public SimpleText(float x, float y, float width, float height, String text, Color color)
    {
        this(x, y, width, height, text, color, TrueTypeFont.ALIGN_CENTER);
    }
    
    public SimpleText(float x, float y, float width, float height, String text, int alignment)
    {
        this(x, y, width, height, text, TEXT_COLOR, alignment);
    }
    
    public SimpleText(float x, float y, float width, float height, String text)
    {
        this(x, y, width, height, text, TEXT_COLOR, TrueTypeFont.ALIGN_CENTER);
    }
}
