package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.game.Game;
import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.gui.exp.UITextField;
import net.lodoma.lime.gui.exp.clean.CleanButton;
import net.lodoma.lime.gui.exp.clean.CleanTextField;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Vector2;

public class MultiplayerMenu extends Stage
{
    private class JoinListener implements UICallback
    {
        @Override
        public void call()
        {
            manager.push(new Game(hostField.text.text));
        }
    }
    
    private class BackListener implements UICallback
    {
        @Override
        public void call()
        {
            manager.pop();
        }
    }
    
    private UITextField hostField;
    
    public MultiplayerMenu()
    {
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.44f), new Vector2(0.4f, 0.05f), "Join", new JoinListener()));
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.26f), new Vector2(0.4f, 0.05f), "Back", new BackListener()));
        ui.children.add(hostField = new CleanTextField(new Vector2(0.05f, 0.5f), new Vector2(0.4f, 0.05f), "localhost"));
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
