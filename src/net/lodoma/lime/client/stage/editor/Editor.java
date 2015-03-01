package net.lodoma.lime.client.stage.editor;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.texture.TexturePool;

public class Editor extends Stage
{
    public Editor()
    {
        
    }
    
    @Override
    public void onActive()
    {
        super.onActive();

        TexturePool.add("editor/window_close");
        TexturePool.add("editor/window_shade");
    }
    
    @Override
    public void onInactive()
    {
        super.onInactive();
        
        TexturePool.clear();
    }
    
    @Override
    public void update(double timeDelta)
    {
        Input.update();
        if (Input.getKey(Input.KEY_ESCAPE))
            manager.pop();
        
        super.update(timeDelta);
    }
    
    @Override
    public void render()
    {
        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        
        super.render();
    }
}
