package net.lodoma.lime.client.stage.menu;

import java.net.InetAddress;

import net.lodoma.lime.client.ClientBroadcast;
import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.stage.game.Game;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.rui.RUIEventData;
import net.lodoma.lime.rui.RUIEventType;
import net.lodoma.lime.rui.RUIGroup;
import net.lodoma.lime.rui.RUIToggle;
import net.lodoma.lime.rui.RUIUnorderedList;
import net.lodoma.lime.rui.RUIValue;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Color;

public class MultiplayerMenu extends Stage
{
    private RUIGroup group = new RUIGroup();
    
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
    }
    
    private void searchLAN()
    {
        selected = null;
        RUIUnorderedList ul = (RUIUnorderedList) rui.getChildRecursive("body.ulServerList");
        for (String childName : ul.getChildrenNames())
            if (childName.startsWith("/"))
                ul.removeChild(childName);
        
        new ClientBroadcast((InetAddress address) -> {
            for (String childName : ul.getChildrenNames())
                if (childName.equals(address.toString()))
                    ul.removeChild(childName);
            
            RUIToggle toggle = new RUIToggle(ul);
            toggle.values.set("default", "index", new RUIValue(10));
            toggle.values.set("default", "width", new RUIValue(0.98f));
            toggle.values.set("default", "height", new RUIValue(0.1f));
            toggle.values.set("default", "foreground-color", new RUIValue(new Color(1.0f, 1.0f, 1.0f, 1.0f)));
            toggle.values.set("default", "background-color", new RUIValue(new Color(0.0f, 0.0f, 0.5f, 1.0f)));
            toggle.values.set("hover", "background-color", new RUIValue(new Color(0.2f, 0.2f, 0.5f, 1.0f)));
            toggle.values.set("active", "background-color", new RUIValue(new Color(0.5f, 0.2f, 0.0f, 1.0f)));
            toggle.values.set("active-hover", "background-color", new RUIValue(new Color(0.5f, 0.5f, 0.0f, 1.0f)));
            toggle.values.set("default", "gradient-color", new RUIValue(new Color(0.31f, 0.31f, 0.63f, 1.0f)));
            toggle.values.set("default", "gradient-source-y", new RUIValue(1.0f));
            toggle.values.set("hover", "gradient-color", new RUIValue(new Color(0.5f, 0.5f, 1.0f, 1.0f)));
            toggle.values.set("active", "gradient-color", new RUIValue(new Color(0.63f, 0.5f, 0.13f, 1.0f)));
            toggle.values.set("active-hover", "gradient-color", new RUIValue(new Color(0.75f, 0.75f, 0.19f, 1.0f)));
            toggle.values.set("default", "border-width", new RUIValue(-1.0f));
            toggle.values.set("default", "border-color", new RUIValue(new Color(0.0f, 0.0f, 0.0f, 1.0f)));
            toggle.values.set("hover", "border-color", new RUIValue(new Color(1.0f, 1.0f, 1.0f, 1.0f)));
            toggle.values.set("default", "border-radius-top-left", new RUIValue(-10.0f));
            toggle.values.set("default", "border-radius-top-right", new RUIValue(-10.0f));
            toggle.values.set("default", "border-radius-bottom-left", new RUIValue(-10.0f));
            toggle.values.set("default", "border-radius-bottom-right", new RUIValue(-10.0f));
            toggle.values.set("default", "horizontal-alignment", new RUIValue("center"));
            toggle.values.set("default", "vertical-alignment", new RUIValue("center"));
            toggle.values.set("default", "text", new RUIValue(address.toString()));
            ul.addChild(address.toString(), toggle);
            
            toggle.group = group;
            toggle.eventListener = (RUIEventType type, RUIEventData data) -> { if (type == RUIEventType.ACTIVE) selected = address; };
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
