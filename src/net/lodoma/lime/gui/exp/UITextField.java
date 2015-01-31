package net.lodoma.lime.gui.exp;

import java.util.List;

import net.lodoma.lime.input.Input;

public class UITextField extends UISelectable
{
    public final UIText text;
    
    public UITextField(UIText text)
    {
        this.text = text;
        
        children.add(text);
    }
    
    @Override
    public boolean onSelect(int button)
    {
        return button == Input.MOUSE_BUTTON_1;
    }
    
    @Override
    public boolean onDeselect(int button)
    {
        return button == Input.MOUSE_BUTTON_1;
    }
    
    @Override
    public void update(double timeDelta)
    {
        if (selected)
        {
            String content = text.text;
            
            List<Character> pressedCharacters = Input.getCharList();
            for (Character character : pressedCharacters)
                content += character;
            
            if(content.length() > 0 && Input.getKeyRepeated(Input.KEY_BACKSPACE))
                content = content.substring(0, content.length() - 1);
            
            text.text = content;
        }
        
        super.update(timeDelta);
    }
}
