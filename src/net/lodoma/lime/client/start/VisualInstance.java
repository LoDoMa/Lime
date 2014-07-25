package net.lodoma.lime.client.start;

import java.io.File;

import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.menu.Menu;
import net.lodoma.lime.client.stage.menu.populator.MainMenuPopulator;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.FontHelper;
import net.lodoma.lime.util.Timer;

public class VisualInstance
{
    private StageManager stageManager;
    
    private void init()
    {
        Window.setDimensions(800, 450);
        Window.setFullscreen(false);
        Window.setTitle("Lime");
        Window.setFPS(60);
        Window.setOrtho(16.0f, 12.0f);
        
        Window.open();
        
        FontHelper.registerFont(new File("fonts/mytype.ttf"));
        
        stageManager = new StageManager();
    }
    
    private void loop()
    {
        Menu mainMenu = new Menu(stageManager, new MainMenuPopulator());
        mainMenu.startStage();
        
        Timer timer = new Timer();
        while(!Window.isCloseRequested())
        {
            Window.clear();
            
            timer.update();
            stageManager.update(timer.getDelta());
            stageManager.render();
            
            Window.update();
        }
    }
    
    private void clean()
    {
        stageManager.popAll();
        
        Window.close();
    }
    
    public void run()
    {
        init();
        loop();
        clean();
    }
}
