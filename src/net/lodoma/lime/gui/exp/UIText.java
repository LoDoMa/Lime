package net.lodoma.lime.gui.exp;

import net.lodoma.lime.util.Vector2;

public class UIText extends UIObject
{
    private static final int SIZE = 42;
    
    public final Vector2 scale;
    public String text;
    public UIFont font;
    
    public UIText(Vector2 position, Vector2 scale, String text, UIFont font)
    {
        this.position.set(position);
        this.scale = scale.div(SIZE);
        this.text = text;
        this.font = font;
    }
    
    @Override
    public void render()
    {
        font.ttf.drawString(position.x, position.y, text, scale.x, scale.y, font.alignment);
        
        super.render();
    }
}
