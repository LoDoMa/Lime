package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.gui.exp.clean.CleanButton;
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
    
    public MainMenu()
    {
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.50f), new Vector2(0.4f, 0.05f), "Campaign", TrueTypeFont.ALIGN_CENTER, null));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.44f), new Vector2(0.4f, 0.05f), "Multiplayer", TrueTypeFont.ALIGN_CENTER, new MultiplayerListener()));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.38f), new Vector2(0.4f, 0.05f), "Arcade", TrueTypeFont.ALIGN_CENTER, null));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.32f), new Vector2(0.4f, 0.05f), "Options", TrueTypeFont.ALIGN_CENTER, new OptionsListener()));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.26f), new Vector2(0.4f, 0.05f), "Exit", TrueTypeFont.ALIGN_CENTER, new ExitListener()));
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
