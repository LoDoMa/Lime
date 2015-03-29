package net.lodoma.lime.client.stage.game;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.rui.RUIEventData;
import net.lodoma.lime.rui.RUIEventType;
import net.lodoma.lime.rui.RUIValue;

public class GameMessage extends Stage
{
    public GameMessage(String text)
    {
        rui.load("Message");
        
        rui.getChildRecursive("body.lblMessage").values.set("default", "text", new RUIValue(text));
        rui.getChildRecursive("body.btnBack").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.MOUSE_RELEASE)
                manager.pop();
        };
    }
    
    @Override
    public void update(double timeDelta)
    {
        Input.update();
        super.update(timeDelta);
    }
}
