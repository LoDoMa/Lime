package net.lodoma.lime.client.stage.editor;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.gui.editor.EditorIcon;
import net.lodoma.lime.gui.editor.EditorPanel;
import net.lodoma.lime.gui.editor.EditorWindow;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.resource.ResourcePool;
import net.lodoma.lime.resource.ResourceType;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Vector2;

public class Editor extends Stage
{
    public Editor()
    {
        EditorPanel mainPanel = new EditorPanel(new Vector2(0.0f, 0.95f), new Vector2(1.0f, 0.05f));
        mainPanel.addChild(new EditorIcon(new Vector2(), new Vector2(9.0f / 16.0f, 1.0f), "editor/load", null));
        mainPanel.addChild(new EditorIcon(new Vector2(), new Vector2(9.0f / 16.0f, 1.0f), "editor/save", null));
        ui.addChild(mainPanel);
        
        ui.addChild(new EditorWindow(new Vector2(0.2f, 0.2f), new Vector2(0.5f, 0.5f), ColorPickerPopulator.INSTANCE));
    }
    
    @Override
    public void onActive()
    {
        super.onActive();

        ResourcePool.referenceUp("editor/load", ResourceType.TEXTURE);
        ResourcePool.referenceUp("editor/save", ResourceType.TEXTURE);
        ResourcePool.referenceUp("editor/window_close", ResourceType.TEXTURE);
        ResourcePool.referenceUp("editor/window_shade", ResourceType.TEXTURE);
    }
    
    @Override
    public void onInactive()
    {
        super.onInactive();
        
        ResourcePool.referenceDown("editor/load", ResourceType.TEXTURE);
        ResourcePool.referenceDown("editor/save", ResourceType.TEXTURE);
        ResourcePool.referenceDown("editor/window_close", ResourceType.TEXTURE);
        ResourcePool.referenceDown("editor/window_shade", ResourceType.TEXTURE);
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
