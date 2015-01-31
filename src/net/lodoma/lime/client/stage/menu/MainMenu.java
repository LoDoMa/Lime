package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.gui.exp.clean.CleanButton;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
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
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.50f), new Vector2(0.4f, 0.05f), "Campaign", null));
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.44f), new Vector2(0.4f, 0.05f), "Multiplayer", new MultiplayerListener()));
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.38f), new Vector2(0.4f, 0.05f), "Arcade", null));
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.32f), new Vector2(0.4f, 0.05f), "Options", new OptionsListener()));
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.26f), new Vector2(0.4f, 0.05f), "Exit", new ExitListener()));
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
        Program.menuProgram.useProgram();
        Program.menuProgram.setUniform("texture", UniformType.INT1, 0);
        
        super.render();
    }
}
