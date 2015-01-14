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
        
        for(int i = 0; i < Input.SIZE_KEYBOARD; i++)
            if(Input.getKeyRepeated(i))
                if(Input.getChar(i) != 0)
                    if(Input.getKey(Input.KEY_LEFT_SHIFT)) content += Character.toUpperCase(Input.getChar(i));
                    else content += Input.getChar(i);
        if(content.length() > 0 && Input.getKeyRepeated(Input.KEY_BACKSPACE))
            content = content.substring(0, content.length() - 1);
        
        text.setText(content);
    }
    
    @Override
    public void render()
    {
        text.render();
    }
}
