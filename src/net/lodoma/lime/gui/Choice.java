package net.lodoma.lime.gui;

import net.lodoma.lime.util.Vector2;

public class Choice implements GUIElement
{
    private class ChangeListener implements ButtonListener
    {
        private final int direction;
        
        public ChangeListener(int direction)
        {
            this.direction = direction;
        }
        
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            onChange(direction);
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private class SetListener implements ButtonListener
    {
        @Override
        public void onClick(Button button, Vector2 mousePosition)
        {
            onSet();
        }
        
        @Override
        public void onHover(Button button, Vector2 mousePosition) {}
    }
    
    private Button prev;
    private Button set;
    private Button next;
    
    private int choiceCount;
    private int current;
    private boolean looping;

    private ChoiceListener listener;
    
    public Choice(Button prev, Button set, Button next, int choiceCount, int current, boolean looping, ChoiceListener listener)
    {
        this.prev = prev;
        this.set = set;
        this.next = next;
        
        prev.setListener(new ChangeListener(-1));
        next.setListener(new ChangeListener(1));
        set.setListener(new SetListener());

        this.choiceCount = choiceCount;
        this.current = current;
        this.looping = looping;
        
        this.listener = listener;
        
        onChange(0);
    }
    
    public Button getPrev()
    {
        return prev;
    }

    public Button getSet()
    {
        return set;
    }

    public Button getNext()
    {
        return next;
    }

    public int getChoiceCount()
    {
        return choiceCount;
    }

    public int getCurrent()
    {
        return current;
    }

    public boolean isLooping()
    {
        return looping;
    }

    public ChoiceListener getListener()
    {
        return listener;
    }
    
    @Override
    public void create()
    {
        prev.create();
        set.create();
        next.create();
    }
    
    @Override
    public void destroy()
    {
        prev.destroy();
        set.destroy();
        next.destroy();
    }
    
    @Override
    public void update(double timeDelta, Vector2 mousePosition)
    {
        prev.update(timeDelta, mousePosition);
        set.update(timeDelta, mousePosition);
        next.update(timeDelta, mousePosition);
    }
    
    @Override
    public void render()
    {
        prev.render();
        set.render();
        next.render();
    }

    private void onChange(int direction)
    {
        current += direction;
        if(looping)
        {
            while(current < 0)
                current += choiceCount;
            while(current >= choiceCount)
                current -= choiceCount;
        }
        else
        {
            if(current < 0)
                current = 0;
            if(current >= choiceCount)
                current = choiceCount- 1;
        }
        listener.onChange(this, current);
    }
    
    private void onSet()
    {
        listener.onSet(this, current);
    }
}
