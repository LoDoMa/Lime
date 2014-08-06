package net.lodoma.lime.gui;

import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Vector2;

public class TextInput implements GUIElement
{
    private Text text;
    
    public TextInput(Text text)
    {
        this.text = text;
    }
    
    public Text getText()
    {
        return text;
    }
    
    @Override
    public void create()
    {
        text.create();
    }
    
    @Override
    public void destroy()
    {
        text.destroy();
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        text.update(timeDelta, mousePosition);
        
        String content = text.getText();
        
        for(int i = 0; i < Input.KEYCODE_COUNT; i++)
            if(Input.getKeyDown(i))
                if(Input.CHARS[i] != 0)
                    if(Input.getKey(Input.KEY_LSHIFT)) content += Character.toUpperCase(Input.CHARS[i]);
                    else content += Input.CHARS[i];
        if(content.length() > 0 && Input.getKeyDown(Input.KEY_BACK))
            content = content.substring(0, content.length() - 1);
        
        text.setText(content);
    }
    
    @Override
    public void render()
    {
        text.render();
    }
}
