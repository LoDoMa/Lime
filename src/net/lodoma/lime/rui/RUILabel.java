package net.lodoma.lime.rui;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.gui.UIFont;
import net.lodoma.lime.util.TrueTypeFont;

public class RUILabel extends RUIElement
{
    public String text;
    
    public TrueTypeFont font;
    public float fontSize;
    
    public int horalign = TrueTypeFont.ALIGN_LEFT;
    public int veralign = TrueTypeFont.ALIGN_LEFT;
    
    private float fontSize_c;
    
    public RUILabel(RUIElement parent)
    {
        super(parent);
    }
    
    @Override
    public void loadDefinition(RUIParserDefinition definition)
    {
        synchronized (treeLock)
        {
            super.loadDefinition(definition);
            
            text = definition.get("default", "text", "");
            font = new UIFont(definition.get("default", "font-name", "FreeSans"), 0, 0).ttf;
            fontSize = RUIParser.parseSize(definition.get("default", "font-size", "0px"));

            horalign = RUIParser.parseAlignment(definition.get("default", "horizontal-alignment", "left"));
            veralign = RUIParser.parseAlignment(definition.get("default", "vertical-alignment", "top"));
        }
    }
    
    @Override
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            super.update(timeDelta);
            
            float fontSize_t = fontSize;
            if (fontSize_t < 0) fontSize_t /= -Window.viewportHeight;
            else fontSize_t *= dimensions_c.y;
            fontSize_c = fontSize_t;
        }
    }
    
    @Override
    protected void renderForeground()
    {
        super.renderForeground();
        
        if (text != null && font != null)
        {
            int ttfSize = font.getFont().getSize() + 3;
            float scale = fontSize_c / ttfSize;
            
            float x, y;
            
            if (horalign == TrueTypeFont.ALIGN_LEFT) x = 0.0f;
            else if (horalign == TrueTypeFont.ALIGN_RIGHT) x = dimensions_c.x;
            else x = dimensions_c.x * 0.5f;
            
            if (veralign == TrueTypeFont.ALIGN_LEFT) y = dimensions_c.y - fontSize_c;
            else if (veralign == TrueTypeFont.ALIGN_RIGHT) y = 0.0f;
            else y = (dimensions_c.y - fontSize_c) / 2.0f;
            
            fgColor_c.setGL();
            font.drawString(x, y, text, scale * 0.6f, scale * 0.75f, horalign);
        }
    }
}
