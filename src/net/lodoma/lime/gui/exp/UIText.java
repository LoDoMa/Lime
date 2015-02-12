package net.lodoma.lime.gui.exp;

import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class UIText extends UIObject
{
    public String text;
    
    private final UIFont font;
    private final Vector2 size = new Vector2(0.0f, 0.0f);
    private final int fontSize;
    
    public UIText(String text, UIFont font, Vector2 size)
    {
        this.text = text;
        this.font = font;
        
        this.size.set(size);
        fontSize = font.ttf.getFont().getSize() + 3;
    }
    
    /* A lot of Vector2 objects are instantiated during execution.
       This helps lower that number down. It doesn't help thread
       safety. UI isn't thread safe. */
    private final Vector2 cacheVector = new Vector2();
    
    @Override
    public Vector2 getLocalPosition()
    {
        cacheVector.set(localPosition);
        
        if (hasParent())
        {
            Vector2 parentDimensions = getParent().getLocalDimensions();
            
            float alignmentMultiplier = 0.0f;
            if (font.alignment == TrueTypeFont.ALIGN_CENTER)
                alignmentMultiplier = 0.5f;
            else if (font.alignment == TrueTypeFont.ALIGN_RIGHT)
                alignmentMultiplier = 1.0f;
            
            cacheVector.addLocalX(parentDimensions.x * alignmentMultiplier);
            cacheVector.addLocalY((parentDimensions.y - size.y) / 2.0f);
        }
        
        return cacheVector;
    }
    
    @Override
    public void render()
    {
        float scaleX = size.x / fontSize;
        float scaleY = size.y / fontSize;
        
        // Note that color and shader aren't set in this class.
        
        Vector2 position = getPosition();
        font.ttf.drawString(position.x, position.y, text, scaleX, scaleY, font.alignment);
        
        super.render();
    }
}
