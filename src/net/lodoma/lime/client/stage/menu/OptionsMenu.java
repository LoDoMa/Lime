package net.lodoma.lime.client.stage.menu;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.stage.Stage;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.client.window.WindowException;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.localization.Language;
import net.lodoma.lime.rui.RUIActivable;
import net.lodoma.lime.rui.RUIChoice;
import net.lodoma.lime.rui.RUIEventData;
import net.lodoma.lime.rui.RUIEventType;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;

public class OptionsMenu extends Stage
{
    public OptionsMenu()
    {
        rui.load("OptionsMenu");
        
        if (Window.fullscreen)
            ((RUIActivable) rui.getChildRecursive("body.ctnrFullscr.toglFullscrOn")).setActivated(true);
        else
            ((RUIActivable) rui.getChildRecursive("body.ctnrFullscr.toglFullscrOff")).setActivated(true);
        
        if (Window.vsync)
            ((RUIActivable) rui.getChildRecursive("body.ctnrVSync.toglVSyncOn")).setActivated(true);
        else
            ((RUIActivable) rui.getChildRecursive("body.ctnrVSync.toglVSyncOff")).setActivated(true);
        
        if (Window.debugEnabled)
            ((RUIActivable) rui.getChildRecursive("body.ctnrDebug.toglDebugOn")).setActivated(true);
        else
            ((RUIActivable) rui.getChildRecursive("body.ctnrDebug.toglDebugOff")).setActivated(true);
        
        rui.getChildRecursive("body.ctnrFullscr.toglFullscrOn").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.ACTIVE)
            {
                Window.fullscreen = true;
                try
                {
                    Window.recreate();
                }
                catch(WindowException e)
                {
                    Lime.LOGGER.C("Failed to recreate the window");
                    Lime.LOGGER.log(e);
                    Lime.forceExit(e);
                }
            }
        };
        
        rui.getChildRecursive("body.ctnrFullscr.toglFullscrOff").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.ACTIVE)
            {
                Window.fullscreen = false;
                try
                {
                    Window.recreate();
                }
                catch(WindowException e)
                {
                    Lime.LOGGER.C("Failed to recreate the window");
                    Lime.LOGGER.log(e);
                    Lime.forceExit(e);
                }
            }
        };
        
        rui.getChildRecursive("body.ctnrVSync.toglVSyncOn").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.ACTIVE)
            {
                Window.vsync = true;
                Window.updateSyncInterval();
            }
        };
        
        rui.getChildRecursive("body.ctnrVSync.toglVSyncOff").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.ACTIVE)
            {
                Window.vsync = false;
                Window.updateSyncInterval();
            }
        };
        
        rui.getChildRecursive("body.ctnrDebug.toglDebugOn").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.ACTIVE)
            {
                Window.debugEnabled = true;
                Lime.LOGGER.F("debug = " + Window.debugEnabled);
            }
        };
        
        rui.getChildRecursive("body.ctnrDebug.toglDebugOff").eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.ACTIVE)
            {
                Window.debugEnabled = false;
                Lime.LOGGER.F("debug = " + Window.debugEnabled);
            }
        };
        
        RUIChoice choiceLanguage = (RUIChoice) rui.getChildRecursive("body.choiceLanguage");
        choiceLanguage.getChoiceList().addAll(Language.getLanguageNameList());
        choiceLanguage.eventListener = (RUIEventType type, RUIEventData data) -> {
            if (type == RUIEventType.CHOICE_CHANGE)
                Language.selectLanguage(choiceLanguage.getChoiceList().get(choiceLanguage.getChoiceIndex()));
        };
        
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
    
    @Override
    public void render()
    {
        Program.basicProgram.useProgram();
        Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
        
        super.render();
    }
}
