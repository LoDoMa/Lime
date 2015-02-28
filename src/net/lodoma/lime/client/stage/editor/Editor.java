package net.lodoma.lime.client.stage.editor;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.input.Input;

public class Editor extends Stage
{
    public Editor()
    {
        
    }
    
    @Override
    public void onActive()
    {
        super.onActive();
    }
    
    @Override
    public void onInactive()
    {
        super.onInactive();
    }
    
    @Override
    public void update(double timeDelta)
    {
        Input.update();
        if(Input.getKey(Input.KEY_ESCAPE))
            manager.pop();
        
        super.update(timeDelta);
    }
    
    @Override
    public void render()
    {
        super.render();
    }
}
