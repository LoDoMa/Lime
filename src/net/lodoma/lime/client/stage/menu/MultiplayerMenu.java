package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.gui.exp.clean.CleanButton;
import net.lodoma.lime.gui.exp.clean.CleanText;
import net.lodoma.lime.gui.exp.clean.CleanUI;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class MultiplayerMenu extends Stage
{
    private class ConnectListener implements UICallback
    {
        @Override
        public void call()
        {
            if (selectedServer == null)
                return;
            manager.push(new MultiplayerDirectConnectMenu());
        }
    }
    
    private class DirectConnectListener implements UICallback
    {
        @Override
        public void call()
        {
            manager.push(new MultiplayerDirectConnectMenu());
        }
    }

    private class SearchLANListener implements UICallback
    {
        @Override
        public void call()
        {
            
        }
    }

    private class AddServerListener implements UICallback
    {
        @Override
        public void call()
        {
            
        }
    }

    private class ModifyServerListener implements UICallback
    {
        @Override
        public void call()
        {
            
        }
    }

    private class RemoveServerListener implements UICallback
    {
        @Override
        public void call()
        {
            
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

    private String selectedServer;
    
    public MultiplayerMenu()
    {
        ui.addChild(new CleanText(new Vector2(0.05f, 0.83f), 0.06f, "Multiplayer", CleanUI.FOCUS_TEXT_COLOR, TrueTypeFont.ALIGN_LEFT));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.20f), new Vector2(0.25f, 0.05f), "Connect", TrueTypeFont.ALIGN_CENTER, new ConnectListener()));
        ui.addChild(new CleanButton(new Vector2(0.35f, 0.20f), new Vector2(0.25f, 0.05f), "Direct", TrueTypeFont.ALIGN_CENTER, new DirectConnectListener()));
        ui.addChild(new CleanButton(new Vector2(0.65f, 0.20f), new Vector2(0.25f, 0.05f), "Search LAN", TrueTypeFont.ALIGN_CENTER, new SearchLANListener()));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.14f), new Vector2(0.25f, 0.05f), "Add", TrueTypeFont.ALIGN_CENTER, new AddServerListener()));
        ui.addChild(new CleanButton(new Vector2(0.35f, 0.14f), new Vector2(0.25f, 0.05f), "Modify", TrueTypeFont.ALIGN_CENTER, new ModifyServerListener()));
        ui.addChild(new CleanButton(new Vector2(0.65f, 0.14f), new Vector2(0.25f, 0.05f), "Remove", TrueTypeFont.ALIGN_CENTER, new RemoveServerListener()));
        ui.addChild(new CleanButton(new Vector2(0.05f, 0.08f), new Vector2(0.8f, 0.05f), "Back", TrueTypeFont.ALIGN_CENTER, new BackListener()));
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
