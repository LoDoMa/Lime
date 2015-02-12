package net.lodoma.lime.gui.exp.clean;

import java.awt.Font;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.exp.UIFont;
import net.lodoma.lime.gui.exp.UIText;
import net.lodoma.lime.util.Vector2;

public class CleanText extends UIText
{
    public Color color;

    public CleanText(Vector2 position, float size, String text, int alignment)
    {
        this(position, size, text, null, alignment);
    }
    
    public CleanText(Vector2 position, float size, String text, Color color, int alignment)
    {
        super(position, new Vector2(size * 0.6f, size * 0.75f), text, new UIFont("My type of font", 42, Font.PLAIN, alignment));
        this.color = color;
    }
    
    @Override
    public void render()
    {
        if (color != null)
            color.setGL();
        
        super.render();
    }
}
