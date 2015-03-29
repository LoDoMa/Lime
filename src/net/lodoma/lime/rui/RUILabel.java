package net.lodoma.lime.rui;

import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.gui.UIFont;
import net.lodoma.lime.util.TrueTypeFont;

public class RUILabel extends RUIElement
{
    protected String fontName_c;
    protected TrueTypeFont font_c;
    protected float fontSize_c;
    protected float fontScaleX_c;
    protected float fontScaleY_c;
    protected int horalign_c;
    protected int veralign_c;
    
    public RUILabel(RUIElement parent)
    {
        super(parent);
    }
    
    @Override
    protected void loadDefaultValues()
    {
        synchronized (treeLock)
        {
            super.loadDefaultValues();
            
            values.set("default", "text", new RUIValue(""));
            values.set("default", "font-name", new RUIValue("FreeSans"));
            values.set("default", "font-size", RUIValue.SIZE_0);
            values.set("default", "horizontal-alignment", new RUIValue("left"));
            values.set("default", "vertical-alignment", new RUIValue("top"));
        }
    }
    
    @Override
    public void loadData(RUIParserData data)
    {
        synchronized (treeLock)
        {
            super.loadData(data);

            data.copy("text", RUIValueType.STRING, values);
            data.copy("font-name", RUIValueType.STRING, values);
            data.copy("font-size", RUIValueType.SIZE, values);
            data.copy("horizontal-alignment", RUIValueType.STRING, values);
            data.copy("vertical-alignment", RUIValueType.STRING, values);
        }
    }
    
    @Override
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            super.update(timeDelta);

            String fontName = values.get(state, "font-name").toString();
            if (fontName_c == null || !fontName_c.equals(fontName))
            {
                fontName_c = fontName;
                font_c = new UIFont(fontName, 0, 0).ttf; // TODO: RUI alternative
            }
            
            float fontSize_t = values.get(state, "font-size").toSize();
            if (fontSize_t < 0) fontSize_t /= -Window.viewportHeight;
            else fontSize_t *= dimensions_c.y;
            fontSize_c = fontSize_t;
            
            int ttfSize = font_c.getFont().getSize() + 3;
            float scale = fontSize_c / ttfSize;
            fontScaleX_c = scale * 0.6f;
            fontScaleY_c = scale * 0.75f;

            String horalign_t = values.get(state, "horizontal-alignment").toString();
            switch (horalign_t)
            {
            case "left": horalign_c = RUIAlignment.ALIGN_LEFT; break;
            case "center": horalign_c = RUIAlignment.ALIGN_CENTER; break;
            case "right": horalign_c = RUIAlignment.ALIGN_RIGHT; break;
            default: throw new IllegalStateException();
            }

            String veralign_t = values.get(state, "vertical-alignment").toString();
            switch (veralign_t)
            {
            case "top": veralign_c = RUIAlignment.ALIGN_TOP; break;
            case "center": veralign_c = RUIAlignment.ALIGN_CENTER; break;
            case "bottom": veralign_c = RUIAlignment.ALIGN_BOTTOM; break;
            default: throw new IllegalStateException();
            }
        }
    }
    
    @Override
    protected void renderForeground()
    {
        super.renderForeground();
        
        String text = values.get(state, "text").toString();
        
        float x, y;
        
        if (horalign_c == RUIAlignment.ALIGN_LEFT) x = 0.0f;
        else if (horalign_c == RUIAlignment.ALIGN_RIGHT) x = dimensions_c.x;
        else x = dimensions_c.x * 0.5f;
        
        if (veralign_c == RUIAlignment.ALIGN_LEFT) y = dimensions_c.y - fontSize_c;
        else if (veralign_c == RUIAlignment.ALIGN_RIGHT) y = 0.0f;
        else y = (dimensions_c.y - fontSize_c) / 2.0f;
        
        fgColor_c.setGL();
        font_c.drawString(x, y, text, fontScaleX_c, fontScaleY_c, horalign_c);
    }
}
