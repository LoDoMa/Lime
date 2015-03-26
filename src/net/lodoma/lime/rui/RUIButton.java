package net.lodoma.lime.rui;

import net.lodoma.lime.gui.UIFont;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class RUIButton extends RUIElement
{
    public RUIEventListener eventListener;
    
    public final RUILabel label;

    public final Color fgColorDefault = new Color();
    public final Color bgColorDefault = new Color();
    public final Color fgColorHover = new Color();
    public final Color bgColorHover = new Color();
    
    private boolean hover;
    
    public RUIButton(RUIElement parent)
    {
        super(parent);
        
        label = new RUILabel(this);
        label.position.set(0.0f, 0.0f);
        label.dimensions.set(1.0f, 1.0f);
        label.text = "";
        label.horalign = TrueTypeFont.ALIGN_CENTER;
        label.veralign = TrueTypeFont.ALIGN_CENTER;
        label.fontSize = 1.0f;
        label.visible = true;
        
        addChild("$LABEL", label);
    }
    
    @Override
    public void loadDefinition(RUIParserDefinition definition)
    {
        synchronized (treeLock)
        {
            super.loadDefinition(definition);

            fgColorDefault.set(fgColor);
            bgColorDefault.set(bgColor);

            fgColorHover.set(RUIParser.parseColor(definition.get("hover", "foreground-color", "00000000")));
            bgColorHover.set(RUIParser.parseColor(definition.get("hover", "background-color", "00000000")));
            
            label.text = definition.get("default", "text", "");
            label.font = new UIFont(definition.get("default", "font-name", "FreeSans"), 0, 0).ttf;
            label.fontSize = RUIParser.parseSize(definition.get("default", "font-size", "100%"));
            label.horalign = RUIParser.parseAlignment(definition.get("default", "horizontal-alignment", "center"));
            label.veralign = RUIParser.parseAlignment(definition.get("default", "vertical-alignment", "center"));
        }
    }
    
    @Override
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            super.update(timeDelta);
            
            boolean prevhover = hover;
            
            Vector2 mousePos = Input.getMousePosition();
            
            hover = false;
            if (mousePos.x >= position_c.x && mousePos.y >= position_c.y)
                if (mousePos.x - position_c.x <= dimensions_c.x && mousePos.y - position_c.y <= dimensions_c.y)
                    hover = true;
            
            if (eventListener != null)
            {
                if (hover && !prevhover) eventListener.onEvent(RUIEventType.MOUSE_HOVER_ON, null);
                else if (!hover && prevhover) eventListener.onEvent(RUIEventType.MOUSE_HOVER_OFF, null);
                
                if (hover)
                {
                    if (Input.getMouseDown(Input.MOUSE_BUTTON_LEFT))
                        eventListener.onEvent(RUIEventType.MOUSE_PRESS, null);
                    else if (Input.getMouseUp(Input.MOUSE_BUTTON_LEFT))
                        eventListener.onEvent(RUIEventType.MOUSE_RELEASE, null);
                }
            }
            
            if (hover)
            {
                fgColor.set(fgColorHover);
                bgColor.set(bgColorHover);
            }
            else
            {
                fgColor.set(fgColorDefault);
                bgColor.set(bgColorDefault);
            }
            
            label.fgColor.set(fgColor);
        }
    }
}
