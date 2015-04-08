package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.editor.Editor;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.rui.RUIEventData;
import net.lodoma.lime.rui.RUIEventType;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;

public class MainMenu extends Stage
{
    public MainMenu()
    {
        rui.load("MainMenu");
        
        rui.getChildRecursive("body.ulMenu.btnMultiplayer").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.push(new MultiplayerMenu());
        };
        
        rui.getChildRecursive("body.ulMenu.btnEditor").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.push(new Editor());
        };
        
        rui.getChildRecursive("body.ulMenu.btnOptions").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.push(new OptionsMenu());
        };
        
        rui.getChildRecursive("body.ulMenu.btnExit").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
                Window.closeRequested = true;
        };
    }
    
    @Override
    public void update(double timeDelta)
    { 
        Input.update();
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
