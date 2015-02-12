package net.lodoma.lime.gui.exp.clean;

import java.awt.Font;

import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.exp.UIFont;
import net.lodoma.lime.gui.exp.UIText;
import net.lodoma.lime.util.Vector2;

public class CleanText extends UIText
{
    private final Color color;
    
    public CleanText(float size, String text, Color color, int alignment)
    {
        this(new Vector2(0.0f), size, text, color, alignment);
    }
    
    public CleanText(Vector2 position, float size, String text, Color color, int alignment)
    {
        super(text, new UIFont("My type of font", 42, Font.PLAIN, alignment), new Vector2(size * 0.6f, size * 0.75f));
        /* NOTE: we set the local position directly,
                 because we can't use getLocalPosition to do it,
                 UIText overrides it. */
        localPosition.set(position);
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
