package net.lodoma.lime.client.stage.menu;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.game.Game;
import net.lodoma.lime.client.stage.game.GameMessage;
import net.lodoma.lime.gui.UITextField;
import net.lodoma.lime.gui.clean.CleanTextField;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.rui.RUIEventData;
import net.lodoma.lime.rui.RUIEventType;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class MultiplayerDirectConnectMenu extends Stage
{
    private UITextField hostField;
    
    public MultiplayerDirectConnectMenu()
    {
        rui.load("DirectConnect");
        
        rui.getChildRecursive("body.btnJoin").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
            {
                manager.pop();
                try
                {
                    manager.push(new Game(InetAddress.getByName(hostField.text.text)));
                }
                catch(UnknownHostException e)
                {
                    Lime.LOGGER.W("Invalid direct connect address; unknown host");
                    
                    manager.push(new GameMessage(e.getClass().getSimpleName() + ": " + e.getMessage()));
                }
            }
        };
        
        rui.getChildRecursive("body.btnBack").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.pop();
        };

        // TODO: replace old UI text field with new RUI alternative
        ui.addChild(hostField = new CleanTextField(new Vector2(0.15f, 0.44f), new Vector2(0.8f, 0.05f), "localhost", TrueTypeFont.ALIGN_LEFT));
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
