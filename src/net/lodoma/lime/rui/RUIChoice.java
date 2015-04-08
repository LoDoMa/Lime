package net.lodoma.lime.rui;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.input.Input;

public class RUIChoice extends RUIButton
{
    private int currentChoice;
    private final List<String> choices = new ArrayList<String>();
    
    public RUIChoice(RUIElement parent)
    {
        super(parent);
    }
    
    @Override
    protected void loadDefaultValues()
    {
        synchronized (treeLock)
        {
            super.loadDefaultValues();
            
            values.set("default", "choices", new RUIValue(""));
        }
    }
    
    @Override
    public void loadData(RUIParserData data)
    {
        synchronized (treeLock)
        {
            super.loadData(data);
            
            String choiceString = data.get("choices");
            if (choiceString != null)
            {
                String[] choiceList = choiceString.split(":");
                for (String choice : choiceList)
                    choices.add(choice);
            }
        }
    }
    
    public int getChoiceIndex()
    {
        return currentChoice;
    }
    
    public void setChoiceIndex(int currentChoice)
    {
        if (currentChoice < 0) currentChoice = 0;
        if (currentChoice >= choices.size()) currentChoice = choices.size() - 1;
        this.currentChoice = currentChoice;
    }
    
    public List<String> getChoiceList()
    {
        return choices;
    }
    
    @Override
    protected void checkClick()
    {
        super.checkClick();
        
        if (Input.getMouseDown(Input.MOUSE_BUTTON_LEFT))
            if (choices.size() > 1)
            {
                currentChoice++;
                if (currentChoice >= choices.size())
                    currentChoice = 0;
                if (eventListener != null)
                    eventListener.onEvent(RUIEventType.CHOICE_CHANGE, null);
            }
    }
    
    @Override
    public void update(double timeDelta)
    {
        super.update(timeDelta);
        
        if (choices.size() > 0)
            values.set("default", "text", new RUIValue(choices.get(currentChoice)));
    }
}
