package net.lodoma.lime.gui.clean;

import net.lodoma.lime.gui.UIFont;
import net.lodoma.lime.gui.UIText;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class CleanText extends UIText
{
    private final Color color;
    
    public CleanText(Vector2 dimensions, String text, Color color, int alignment)
    {
        this(new Vector2(0.0f), dimensions, text, color, alignment);
    }
    
    public CleanText(Vector2 position, Vector2 dimensions, String text, Color color, int alignment)
    {
        super(text, new UIFont("FreeSans", alignment, TrueTypeFont.ALIGN_CENTER), new Vector2(dimensions.y * 0.6f, dimensions.y * 0.75f));
        
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        this.color = color;
    }
    
    public CleanText(Vector2 position, float height, String text, Color color, int alignment)
    {
        this(position, height, text, color, alignment, new UIFont("FreeSans", alignment, TrueTypeFont.ALIGN_CENTER));
    }
    
    private CleanText(Vector2 position, float height, String text, Color color, int alignement, UIFont font)
    {
        super(text, font, new Vector2(height * 0.6f, height * 0.75f));

        getLocalPosition().set(position);
        getLocalDimensions().set(font.ttf.getTotalWidth(text), height);
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
