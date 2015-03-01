package net.lodoma.lime.gui;

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
    
    @Override
    public void update(double timeDelta)
    {
        if (getParent() == null)
            throw new IllegalStateException("UIText must have a parent");
        
        super.update(timeDelta);
    }
    
    @Override
    public void render()
    {
        float scaleX = size.x / fontSize;
        float scaleY = size.y / fontSize;

        Vector2 dimensions = getDimensions();
        
        float horMul = 0.0f;
        if (font.horizontalAlignment == TrueTypeFont.ALIGN_CENTER)
            horMul = 0.5f;
        else if (font.horizontalAlignment == TrueTypeFont.ALIGN_RIGHT)
            horMul = 1.0f;
        
        float verMul = 0.0f;
        if (font.verticalAlignment == TrueTypeFont.ALIGN_CENTER)
            verMul = 0.5f;
        else if (font.verticalAlignment == TrueTypeFont.ALIGN_RIGHT)
            verMul = 1.0f;

        float horOffset = horMul * dimensions.x;
        float verOffset = verMul * dimensions.y - verMul * fontSize * scaleY;
        
        Vector2 position = getPosition();

        // Note that color and shader aren't set in this class.
        
        font.ttf.drawString(position.x + horOffset, position.y + verOffset, text, scaleX, scaleY, font.horizontalAlignment);
        
        super.render();
    }
}
