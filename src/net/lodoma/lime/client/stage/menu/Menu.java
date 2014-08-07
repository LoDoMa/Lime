package net.lodoma.lime.client.stage.menu;

import java.io.File;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.menu.populator.MenuPopulator;
import net.lodoma.lime.gui.GUIContainer;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.Shader;
import net.lodoma.lime.shader.ShaderType;
import net.lodoma.lime.shader.UniformType;

public class Menu extends Stage
{
    private GUIContainer container;
    private MenuPopulator initialPopulator;
    private MenuPopulator populator;
    
    private Program menuProgram;
    
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

        Input.update();
        container.update(timeDelta);
    }
    
    @Override
    public void render()
    {
        if(menuProgram == null)
        {
            Shader menuVS = Shader.getShader("menuVS", new File("shader/menu.vs"), ShaderType.VERTEX);
            Shader menuFS = Shader.getShader("menuFS", new File("shader/menu.fs"), ShaderType.FRAGMENT);
            menuProgram = Program.getProgram("menu", menuVS, menuFS);
        }
        menuProgram.useProgram();
        menuProgram.setUniform("texture", UniformType.INT1, 0);
        
        container.render();
    }
}
