package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.rui.RUIElement;
import net.lodoma.lime.rui.RUIEventData;
import net.lodoma.lime.rui.RUIEventListener;
import net.lodoma.lime.rui.RUIEventType;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;

public class SuperFancyTest extends Stage
{
    private RUIElement root;
    
    public SuperFancyTest()
    {
        
    }
    
    @Override
    public void onActive()
    {
        super.onActive();
        
        root = new RUIElement(null);
        root.load("MainMenu");
        
        root.getChildRecursive("cont1.cont2.test5").eventListener = new RUIEventListener()
        {
            @Override
            public void onEvent(RUIEventType type, RUIEventData data)
            {
                System.out.println(type.name());
            }
        };
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
        root.update(timeDelta);
        
        if(Input.getKey(Input.KEY_ESCAPE))
            manager.pop();
    }
    
    @Override
    public void render()
    {
        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        
        root.render();
    }
}
