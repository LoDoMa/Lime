package net.lodoma.lime.gui;

import net.lodoma.lime.util.Vector2;

public class Toggle implements GUIElement
{
    private class BasicListener implements ButtonListener
    {
        private boolean state;
        
        public BasicListener(boolean state)
        {
            this.state = state;
        }
        
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            onToggle(state);
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private Text text;
    private boolean current;
    private Button buttonTrue;
    private Button buttonFalse;
    
    private ToggleListener listener;
    
    public Toggle(Text text, boolean current, Button buttonTrue, Button buttonFalse, ToggleListener listener)
    {
        this.text = text;
        this.buttonTrue = buttonTrue;
        this.buttonFalse = buttonFalse;

        buttonTrue.setListener(new BasicListener(true));
        buttonFalse.setListener(new BasicListener(false));
        
        this.listener = listener;
        
        onToggle(current);
    }
    
    public Text getText()
    {
        return text;
    }
    
    public boolean getState()
    {
        return current;
    }
    
    public Button getButton(boolean state)
    {
        return state ? buttonTrue : buttonFalse;
    }
    
    public ToggleListener getListener()
    {
        return listener;
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
    
    private void onToggle(boolean state)
    {
        current = state;
        listener.onToggle(this, state);
    }
}
