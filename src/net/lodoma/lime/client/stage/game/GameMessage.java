package net.lodoma.lime.client.stage.game;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.gui.exp.clean.CleanButton;
import net.lodoma.lime.gui.exp.clean.CleanText;
import net.lodoma.lime.gui.exp.clean.CleanUI;
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
        ui.addChild(new CleanText(new Vector2(0.5f, 0.625f), 0.05f, text, CleanUI.TEXT_COLOR, TrueTypeFont.ALIGN_CENTER));
        ui.addChild(new CleanButton(new Vector2(0.3f, 0.2f), new Vector2(0.4f, 0.05f), "Back", TrueTypeFont.ALIGN_CENTER, new BackListener()));
    }
    
    @Override
    public void update(double timeDelta)
    {
        Input.update();
        super.update(timeDelta);
    }
}
