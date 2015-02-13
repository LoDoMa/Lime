package net.lodoma.lime.client.stage.menu;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.game.Game;
import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.gui.exp.UITextField;
import net.lodoma.lime.gui.exp.clean.CleanButton;
import net.lodoma.lime.gui.exp.clean.CleanText;
import net.lodoma.lime.gui.exp.clean.CleanTextField;
import net.lodoma.lime.gui.exp.clean.CleanUI;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class MultiplayerDirectConnectMenu extends Stage
{
    private class JoinListener implements UICallback
    {
        @Override
        public void call()
        {
            manager.pop();
            try
            {
                manager.push(new Game(InetAddress.getByName(hostField.text.text)));
            }
            catch(UnknownHostException e)
            {
                e.printStackTrace();
            }
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
    
    public MultiplayerDirectConnectMenu()
    {
        ui.addChild(new CleanText(new Vector2(0.05f, 0.6f), 0.06f, "Direct connect", CleanUI.FOCUS_TEXT_COLOR, TrueTypeFont.ALIGN_LEFT));
        ui.addChild(new CleanText(new Vector2(0.05f, 0.465f), 0.05f, "Host:", CleanUI.TEXT_COLOR, TrueTypeFont.ALIGN_LEFT));
        ui.addChild(hostField = new CleanTextField(new Vector2(0.15f, 0.44f), new Vector2(0.8f, 0.05f), "localhost", TrueTypeFont.ALIGN_LEFT));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.34f), new Vector2(0.4f, 0.05f), "Join", TrueTypeFont.ALIGN_CENTER, new JoinListener()));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.26f), new Vector2(0.4f, 0.05f), "Back", TrueTypeFont.ALIGN_CENTER, new BackListener()));
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
