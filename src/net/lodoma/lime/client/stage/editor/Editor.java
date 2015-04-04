package net.lodoma.lime.client.stage.editor;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.gui.editor.EditorIcon;
import net.lodoma.lime.gui.editor.EditorPanel;
import net.lodoma.lime.gui.editor.EditorWindow;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.resource.texture.Texture;
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

        Texture.referenceUp("editor/load");
        Texture.referenceUp("editor/save");
        Texture.referenceUp("editor/window_close");
        Texture.referenceUp("editor/window_shade");
    }
    
    @Override
    public void onInactive()
    {
        super.onInactive();
        
        Texture.referenceDown("editor/load");
        Texture.referenceDown("editor/save");
        Texture.referenceDown("editor/window_close");
        Texture.referenceDown("editor/window_shade");
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
