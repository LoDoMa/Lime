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
    
    public TextField(Rectangle bounds)
    {
        super(bounds);
        text = new Text(bounds.w / 2.0f, 0.0f, (bounds.h * 0.60f), (bounds.h * 0.75f), "", new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_CENTER);
        clicked = false;
    }
    
    public String getText()
    {
        return text.getText();
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
            if(mouseDown)
                clicked = false;
        
        if(clicked)
        {
            for(int i = 0; i < Input.KEYCODE_COUNT; i++)
                if(Input.getKeyDown(i))
                    if(Input.CHARS[i] != 0)
                        if(Input.getKey(Input.KEY_LSHIFT))
                            text.setText(text.getText() + Character.toUpperCase(Input.CHARS[i]));
                        else
                            text.setText(text.getText() + Input.CHARS[i]);
            if(text.getText().length() > 0 && Input.getKeyDown(Input.KEY_BACK))
                text.setText(text.getText().substring(0, text.getText().length() - 1));
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
