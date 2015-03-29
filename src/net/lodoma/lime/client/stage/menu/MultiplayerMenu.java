package net.lodoma.lime.client.stage.menu;

import java.net.InetAddress;
import java.util.Comparator;
import java.util.List;

import net.lodoma.lime.client.ClientBroadcast;
import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.game.Game;
import net.lodoma.lime.gui.clean.CleanList;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.rui.RUIEventData;
import net.lodoma.lime.rui.RUIEventType;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Vector2;

public class MultiplayerMenu extends Stage
{
    private CleanList<InetAddress> list;
    private InetAddress selected;
    
    public MultiplayerMenu()
    {
        rui.load("MultiplayerMenu");
        
        rui.getChildRecursive("body.btnConnect").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE && selected != null)
                manager.push(new Game(selected));
        };
        
        rui.getChildRecursive("body.btnDirect").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.push(new MultiplayerDirectConnectMenu());
        };
        
        rui.getChildRecursive("body.btnSearch").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
                searchLAN();
        };
        
        rui.getChildRecursive("body.btnBack").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.pop();
        };
        
        // TODO: replace old UI list with new RUI alternative
        ui.addChild(list = new CleanList<InetAddress>(new Vector2(0.05f, 0.75f), new Vector2(0.9f, 0.0f), () -> selected = list.getSelectedItem()));
    }
    
    private void searchLAN()
    {
        List<InetAddress> addresses = list.getItemList();
        for (InetAddress address : addresses)
        {
            if (address.toString().startsWith("/"))
            {
                list.removeElement(address, new Comparator<InetAddress>() {
                    @Override
                    public int compare(InetAddress o1, InetAddress o2)
                    {
                        return o1.toString().equals(o2.toString()) ? 0 : 1;
                    }
                });
            }
        }
        
        new ClientBroadcast((InetAddress address) -> {
            list.removeElement(address, new Comparator<InetAddress>() {
                @Override
                public int compare(InetAddress o1, InetAddress o2)
                {
                    return o1.toString().equals(o2.toString()) ? 0 : 1;
                }
            });
            list.addElement(address);
        });
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
