package net.lodoma.lime.gui;

import net.lodoma.lime.util.Vector2;

public class Choice implements GUIElement
{
    protected Button prev;
    protected Button set;
    protected Button next;
    
    protected int current;
    protected String[] choices;

    protected ChoiceListener listener;
    
    public Choice(Button prev, Button set, Button next, String[] choices, ChoiceListener listener)
    {
        this.prev = prev;
        this.set = set;
        this.next = next;
        
        this.listener = listener;

        listener.onChange(choices, current);
        prev.setListener(new Runnable()
        {
            @Override
            public void run()
            {
                current--;
                while(current < 0)
                    current += choices.length;
                listener.onChange(choices, current);
            }
        });
        next.setListener(new Runnable()
        {
            @Override
            public void run()
            {
                current++;
                while(current >= choices.length)
                    current -= choices.length;
                listener.onChange(choices, current);
            }
        });
        set.setListener(new Runnable()
        {
            @Override
            public void run()
            {
                listener.onSet(choices, current);
            }
        });
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
}
