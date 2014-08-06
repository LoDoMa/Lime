package net.lodoma.lime.client.stage.menu;

import java.awt.Font;

import net.lodoma.lime.gui.Button;
import net.lodoma.lime.gui.ButtonListener;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.Rectangle;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class MenuButton extends Button
{
    private static final String FONT_NAME = "My type of font";
    private static final Color TEXT_COLOR = new Color(0.0f, 0.5f, 1.0f);
    
    private Text text;
    
    private MenuButtonRenderer renderer;
    private float transparency;
    
    public MenuButton(Rectangle bounds, ButtonListener listener, String text)
    {
        super(bounds, listener, null);
        
        this.text = new Text(bounds.w / 2.0f, 0.0f, (bounds.h * 0.60f), (bounds.h * 0.75f), text, TEXT_COLOR, FONT_NAME, Font.PLAIN, TrueTypeFont.ALIGN_CENTER);
        
        renderer = new MenuButtonRenderer();
        setRenderer(renderer);
        transparency = 0.0f;
    }
    
    public void setText(String text)
    {
        this.text.setText(text);
    }
    
    @Override
    public void create()
    {
        super.create();
        text.create();
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        text.destroy();
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        super.update(timeDelta, mousePosition);
        
        if(isMouseHovering())
        {
            if(transparency < 1.0f)
            {
                transparency += timeDelta * 5.0f;
                if(transparency > 1.0f)
                    transparency = 1.0f;
            }
        }
        else
            transparency = 0.0f;
        
        renderer.setText(text);
        renderer.setTransparency(transparency);
    }
}
