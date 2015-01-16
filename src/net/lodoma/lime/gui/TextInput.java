package net.lodoma.lime.gui;

import java.util.List;

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
        
        List<Character> pressedCharacters = Input.getCharList();
        for (Character character : pressedCharacters)
            content += character;
        
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
