package net.lodoma.lime.gui.exp.clean;

import java.awt.Font;

import net.lodoma.lime.gui.exp.UIFont;
import net.lodoma.lime.gui.exp.UIText;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class CleanText extends UIText
{
    public CleanText(Vector2 position, float size, String text)
    {
        this(position, size, text, TrueTypeFont.ALIGN_LEFT);
    }
    
    public CleanText(Vector2 position, float size, String text, int alignment)
    {
        super(position, new Vector2(size * 0.6f, size * 0.75f), text, new UIFont("My type of font", 42, Font.PLAIN, alignment));
    }
}
