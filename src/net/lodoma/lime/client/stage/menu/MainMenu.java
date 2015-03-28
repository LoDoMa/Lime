package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.editor.Editor;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.rui.RUIEventData;
import net.lodoma.lime.rui.RUIEventListener;
import net.lodoma.lime.rui.RUIEventType;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;

public class MainMenu extends Stage
{
    private class MultiplayerListener implements RUIEventListener
    {
        @Override
        public void onEvent(RUIEventType type, RUIEventData data)
        {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.push(new MultiplayerMenu());
        }
    }
    
    private class EditorListener implements RUIEventListener
    {
        @Override
        public void onEvent(RUIEventType type, RUIEventData data)
        {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.push(new Editor());
        }
    }
    
    private class OptionsListener implements RUIEventListener
    {
        @Override
        public void onEvent(RUIEventType type, RUIEventData data)
        {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.push(new OptionsMenu());
        }
    }
    
    private class ExitListener implements RUIEventListener
    {
        @Override
        public void onEvent(RUIEventType type, RUIEventData data)
        {
            if (type == RUIEventType.MOUSE_RELEASE)
                Window.closeRequested = true;
        }
    }
    
    public MainMenu()
    {
        rui.load("MainMenu");
        rui.getChildRecursive("body.menu.btnMultiplayer").eventListener = new MultiplayerListener();
        rui.getChildRecursive("body.menu.btnEditor").eventListener = new EditorListener();
        rui.getChildRecursive("body.menu.btnOptions").eventListener = new OptionsListener();
        rui.getChildRecursive("body.menu.btnExit").eventListener = new ExitListener();
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
