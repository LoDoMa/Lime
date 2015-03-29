package net.lodoma.lime.rui;

import java.util.List;

import net.lodoma.lime.input.Input;

public class RUITextField extends RUIButton
{
    private boolean selected = false;
    
    public RUITextField(RUIElement parent)
    {
        super(parent);
    }
    
    @Override
    public void update(double timeDelta)
    {
        synchronized (treeLock)
        {
            super.update(timeDelta);
            
            if (selected && !hover && Input.getMouseDown(Input.MOUSE_BUTTON_LEFT))
                selected = false;
            
            if (selected)
            {
                String text = values.get("default", "text").toString();
                
                List<Character> pressedCharacters = Input.getCharList();
                for (Character character : pressedCharacters)
                    text += character;
                
                if(text.length() > 0 && Input.getKeyRepeated(Input.KEY_BACKSPACE))
                    text = text.substring(0, text.length() - 1);
                
                int cutLength = text.length();
                for (; cutLength > 0; cutLength--)
                    if (font_c.getTotalWidth(text, 0, cutLength - 1) * fontScaleX_c < dimensions_c.x)
                        break;
                text = text.substring(0, cutLength);
                
                values.set("default", "text", new RUIValue(text));
                
                state = "hover";
            }
        }
    }
    
    @Override
    protected void checkClick()
    {
        super.checkClick();
        
        if (!selected && Input.getMouseDown(Input.MOUSE_BUTTON_LEFT))
            selected = true;
    }
}
