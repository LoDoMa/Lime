package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.editor.Editor;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.gui.UICallback;
import net.lodoma.lime.gui.clean.CleanButton;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class MainMenu extends Stage
{
    private class MultiplayerListener implements UICallback
    {
        @Override
        public void call()
        {
            manager.push(new MultiplayerMenu());
        }
    }
    
    private class EditorListener implements UICallback
    {
        @Override
        public void call()
        {
            manager.push(new Editor());
        }
    }
    
    private class OptionsListener implements UICallback
    {
        @Override
        public void call()
        {
            manager.push(new OptionsMenu());
        }
    }
    
    private class ExitListener implements UICallback
    {
        @Override
        public void call()
        {
            Window.closeRequested = true;
        }
    }
    
    private class SuperFancyListener implements UICallback
    {
        @Override
        public void call()
        {
            manager.push(new SuperFancyTest());
        }
    }
    
    public MainMenu()
    {
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.50f), new Vector2(0.4f, 0.05f), "Multiplayer", TrueTypeFont.ALIGN_CENTER, new MultiplayerListener()));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.44f), new Vector2(0.4f, 0.05f), "Editor", TrueTypeFont.ALIGN_CENTER, new EditorListener()));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.38f), new Vector2(0.4f, 0.05f), "Options", TrueTypeFont.ALIGN_CENTER, new OptionsListener()));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.32f), new Vector2(0.4f, 0.05f), "Exit", TrueTypeFont.ALIGN_CENTER, new ExitListener()));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.20f), new Vector2(0.4f, 0.05f), "SuperFancy", TrueTypeFont.ALIGN_CENTER, new SuperFancyListener()));
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
