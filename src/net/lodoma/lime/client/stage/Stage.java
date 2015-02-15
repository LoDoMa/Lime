package net.lodoma.lime.client.stage;

import net.lodoma.lime.gui.UIObject;

public class Stage
{
    public StageManager manager;
    public Stage parent;
    
    public UIObject ui = new UIObject();
    
    public void onActive() {}
    public void onInactive() {}
    
    public void update(double timeDelta)
    {
        ui.update(timeDelta);
    }
    
    public void render()
    {
        ui.render();
    }
}
