package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.ClientBroadcast;
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
            new ClientBroadcast();
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
    
    public MultiplayerMenu()
    {
        ui.children.add(new CleanText(new Vector2(0.05f, 0.8f), 0.06f, "Multiplayer", CleanUI.FOCUS_TEXT_COLOR, TrueTypeFont.ALIGN_LEFT));
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.20f), new Vector2(0.4f, 0.05f), "Direct connect", new DirectConnectListener()));
        ui.children.add(new CleanButton(new Vector2(0.50f, 0.20f), new Vector2(0.4f, 0.05f), "Search LAN", new SearchLANListener()));
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.14f), new Vector2(0.25f, 0.05f), "Add", new AddServerListener()));
        ui.children.add(new CleanButton(new Vector2(0.35f, 0.14f), new Vector2(0.25f, 0.05f), "Modify", new ModifyServerListener()));
        ui.children.add(new CleanButton(new Vector2(0.65f, 0.14f), new Vector2(0.25f, 0.05f), "Remove", new RemoveServerListener()));
        ui.children.add(new CleanButton(new Vector2(0.05f, 0.08f), new Vector2(0.8f, 0.05f), "Back", new BackListener()));
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
