package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.menu.populator.MenuPopulator;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.input.Input;

public class Menu extends Stage
{
    private GUIContainer container;
    private MenuPopulator initialPopulator;
    private MenuPopulator populator;
    
    public Menu(StageManager manager, MenuPopulator initialPopulator)
    {
        super(manager);
        
        container = new GUIContainer();
        this.initialPopulator = initialPopulator;
    }
    
    public void setPopulator(MenuPopulator populator)
    {
        this.populator = populator;
    }
    
    public GUIContainer getContainer()
    {
        return container;
    }
    
    @Override
    public void preStart()
    {
        
    }
    
    @Override
    public void onStart()
    {
        setPopulator(initialPopulator);
    }
    
    @Override
    public void onEnd()
    {
        
    }
    
    @Override
    public void postEnd()
    {
        
    }
    
    @Override
    public void update(double timeDelta)
    {
        if(populator != null)
        {
            container.removeAll();
            populator.populate(this);
            populator = null;
        }
        
        container.update(timeDelta);
        Input.update();
    }
    
    @Override
    public void render()
    {
        container.render();
    }
}
