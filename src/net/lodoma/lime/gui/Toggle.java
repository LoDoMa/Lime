package net.lodoma.lime.gui;

import net.lodoma.lime.util.Vector2;

public class Toggle implements GUIElement
{
    private Text text;
    private String textTrue;
    private String textFalse;
    private Button buttonTrue;
    private Button buttonFalse;
    private ToggleListener listener;
    
    public Toggle(Text text, String textTrue, String textFalse, Button buttonTrue, Button buttonFalse, ToggleListener listener)
    {
        this.text = text;
        this.textTrue = textTrue;
        this.textFalse = textFalse;
        this.buttonTrue = buttonTrue;
        this.buttonFalse = buttonFalse;
        this.listener = listener;
        
        setListeners();
    }
    
    private void setListeners()
    {
        buttonTrue.setListener(new Runnable()
        {
            @Override
            public void run()
            {
                text.setText(textTrue);
                listener.onToggle(true);
            }
        });
        buttonFalse.setListener(new Runnable()
        {
            @Override
            public void run()
            {
                text.setText(textFalse);
                listener.onToggle(false);
            }
        });
    }
    
    @Override
    public void create()
    {
        text.create();
        buttonTrue.create();
        buttonFalse.create();
    }
    
    @Override
    public void destroy()
    {
        text.destroy();
        buttonTrue.destroy();
        buttonFalse.destroy();
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        text.update(timeDelta, mousePosition);
        buttonTrue.update(timeDelta, mousePosition);
        buttonFalse.update(timeDelta, mousePosition);
    }
    
    @Override
    public void render()
    {
        text.render();
        buttonTrue.render();
        buttonFalse.render();
    }
}
