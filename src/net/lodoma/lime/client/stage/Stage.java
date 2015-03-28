package net.lodoma.lime.client.stage;

import net.lodoma.lime.gui.UIObject;
import net.lodoma.lime.rui.RUIElement;

public class Stage
{
    public StageManager manager;
    public Stage parent;
    
    @Deprecated
    public UIObject ui = new UIObject();
    public RUIElement rui = new RUIElement(null);
    
    public void onActive() {}
    public void onInactive() {}
    
    public void update(double timeDelta)
    {
        synchronized (ui)
        {
            ui.update(timeDelta);
        }
        rui.update(timeDelta);
    }
    
    public void render()
    {
        synchronized (ui)
        {
            ui.render();
        }
        rui.render();
    }
}
