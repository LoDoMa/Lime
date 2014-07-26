package net.lodoma.lime.gui;

import java.awt.Font;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

import org.lwjgl.opengl.GL11;

public abstract class TextField extends Button
{
    protected boolean clicked;
    protected Text text;
    protected char mask;
    
    protected String content;
    
    public TextField(Rectangle bounds, char mask)
    {
        super(bounds);
        text = new Text(bounds.w / 2.0f, 0.0f, (bounds.h * 0.60f), (bounds.h * 0.75f), "", new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_CENTER);
        this.mask = mask;
        clicked = false;
        content = "";
    }
    
    public String getText()
    {
        return content;
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        super.update(timeDelta, mousePosition);
        
        if(mouseClick)
        {
            clicked = true;
        }
        else
            if(mouseUp)
                clicked = false;
        
        if(clicked)
        {
            for(int i = 0; i < Input.KEYCODE_COUNT; i++)
                if(Input.getKeyDown(i))
                    if(Input.CHARS[i] != 0)
                        if(Input.getKey(Input.KEY_LSHIFT)) content += Character.toUpperCase(Input.CHARS[i]);
                        else content += Input.CHARS[i];
            if(content.length() > 0 && Input.getKeyDown(Input.KEY_BACK))
                content = content.substring(0, content.length() - 1);
        }
        
        if(mask != (char) 0)
        {
            String masked = "";
            for(int i = 0; i < content.length(); i++)
                masked += mask;
            text.setText(masked);
        }
        else
        {
            text.setText(content);
        }
    }
    
    @Override
    public void render()
    {
        super.render();
        
        GL11.glPushMatrix();
        GL11.glTranslatef(bounds.x, bounds.y, 0.0f);
        text.render();
        GL11.glPopMatrix();
    }
}
