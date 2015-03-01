package net.lodoma.lime.client.stage.editor;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.gui.editor.EditorWindow;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Vector2;

public class Editor extends Stage
{
    public Editor()
    {
        EditorWindow window = new EditorWindow(new Vector2(0.1f, 0.1f), new Vector2(0.5f, 0.5f), "Test window");
        EditorWindow window2 = new EditorWindow(new Vector2(0.1f, 0.1f), new Vector2(0.5f, 0.5f), "Test window 2");
        window.contentPane.addChild(window2);
        ui.addChild(window);
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
        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        
        super.render();
    }
}
