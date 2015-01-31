package net.lodoma.lime.client.stage.game;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.gui.exp.clean.CleanButton;
import net.lodoma.lime.gui.exp.clean.CleanText;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class GameMessage extends Stage
{
    private class BackListener implements UICallback
    {
        @Override
        public void call()
        {
            manager.pop();
        }
    }
    
    public GameMessage(String text)
    {
        ui.children.add(new CleanText(new Vector2(0.5f, 0.6f), 0.05f, text, TrueTypeFont.ALIGN_CENTER));
        ui.children.add(new CleanButton(new Vector2(0.3f, 0.2f), new Vector2(0.4f, 0.05f), "Back", new BackListener()));
    }
    
    @Override
    public void update(double timeDelta)
    {
        Input.update();
        super.update(timeDelta);
    }
}
