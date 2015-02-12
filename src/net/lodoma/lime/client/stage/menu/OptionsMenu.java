package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.client.window.WindowException;
import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.gui.exp.UIGroup;
import net.lodoma.lime.gui.exp.clean.CleanButton;
import net.lodoma.lime.gui.exp.clean.CleanSlider;
import net.lodoma.lime.gui.exp.clean.CleanText;
import net.lodoma.lime.gui.exp.clean.CleanToggle;
import net.lodoma.lime.gui.exp.clean.CleanUI;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class OptionsMenu extends Stage
{
    private class FullscreenListener implements UICallback
    {
        @Override
        public void call()
        {
            Window.fullscreen = !Window.fullscreen;
            fullscreen.setText(Window.fullscreen ? "Fullscreen" : "Windowed");
            
            try
            {
                Window.recreate();
            }
            catch(WindowException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private class VSyncListener implements UICallback
    {
        @Override
        public void call()
        {
            Window.vsync = vsyncGroup.selected == vsyncOn;
            Window.updateSyncInterval();
        }
    }
    
    private class DebugListener implements UICallback
    {
        @Override
        public void call()
        {
            Window.debugEnabled = debugGroup.selected == debugOn;
        }
    }
    
    private class SoundListener implements UICallback
    {
        @Override
        public void call()
        {
            System.out.println("Sound level: " + (int) (sound.sliderValue * 100) + "%");
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

    private UIGroup vsyncGroup;
    private UIGroup debugGroup;
    
    private CleanButton fullscreen;
    private CleanToggle vsyncOn;
    private CleanToggle vsyncOff;
    private CleanToggle debugOn;
    private CleanToggle debugOff;
    private CleanSlider sound;
    
    public OptionsMenu()
    {
        vsyncGroup = new UIGroup(new VSyncListener());
        debugGroup = new UIGroup(new DebugListener());
        
        ui.addChild(fullscreen = new CleanButton(new Vector2(0.05f, 0.50f), new Vector2(0.4f, 0.05f), Window.fullscreen ? "Fullscreen" : "Windowed", TrueTypeFont.ALIGN_CENTER, new FullscreenListener()));
        ui.addChild(new CleanText(new Vector2(0.05f, 0.465f), 0.05f, "VSync:", CleanUI.TEXT_COLOR, TrueTypeFont.ALIGN_LEFT));
        ui.addChild(vsyncOn = new CleanToggle(new Vector2(0.25f, 0.44f), new Vector2(0.1f, 0.05f), "on", vsyncGroup, TrueTypeFont.ALIGN_CENTER));
        ui.addChild(vsyncOff = new CleanToggle(new Vector2(0.35f, 0.44f), new Vector2(0.1f, 0.05f), "off", vsyncGroup, TrueTypeFont.ALIGN_CENTER));
        ui.addChild(new CleanText(new Vector2(0.05f, 0.405f), 0.05f, "Debug:", CleanUI.TEXT_COLOR, TrueTypeFont.ALIGN_LEFT));
        ui.addChild(debugOn = new CleanToggle(new Vector2(0.25f, 0.38f), new Vector2(0.1f, 0.05f), "on", debugGroup, TrueTypeFont.ALIGN_CENTER));
        ui.addChild(debugOff = new CleanToggle(new Vector2(0.35f, 0.38f), new Vector2(0.1f, 0.05f), "off", debugGroup, TrueTypeFont.ALIGN_CENTER));
        ui.addChild(new CleanText(new Vector2(0.05f, 0.346f), 0.05f, "Sound: ", CleanUI.TEXT_COLOR, TrueTypeFont.ALIGN_LEFT));
        ui.addChild(sound = new CleanSlider(new Vector2(0.25f, 0.32f), new Vector2(0.4f, 0.05f), new SoundListener()));
        ui.addChild(new CleanButton(new Vector2(0.3f, 0.26f), new Vector2(0.4f, 0.05f), "Back", TrueTypeFont.ALIGN_CENTER, new BackListener()));

        vsyncGroup.select(Window.vsync ? vsyncOn : vsyncOff);
        debugGroup.select(Window.debugEnabled ? debugOn : debugOff);
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
